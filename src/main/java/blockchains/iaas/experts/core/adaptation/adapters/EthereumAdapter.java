package blockchains.iaas.experts.core.adaptation.adapters;

import blockchains.iaas.experts.core.adaptation.interfaces.BlockchainAdapter;
import blockchains.iaas.experts.core.exceptions.BlockchainNodeUnreachableException;
import blockchains.iaas.experts.core.exceptions.InvalidTransactionException;
import blockchains.iaas.experts.core.model.Block;
import blockchains.iaas.experts.core.model.Transaction;
import blockchains.iaas.experts.core.model.TransactionState;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.web3j.crypto.CipherException;
import org.web3j.crypto.Credentials;
import org.web3j.crypto.WalletUtils;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.methods.response.EthBlock;
import org.web3j.protocol.core.methods.response.EthTransaction;
import org.web3j.protocol.http.HttpService;
import org.web3j.tx.Transfer;
import org.web3j.utils.Convert;
import rx.Observable;
import rx.Subscriber;
import rx.Subscription;
import rx.subjects.PublishSubject;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionException;


public class EthereumAdapter implements BlockchainAdapter {
    private Credentials credentials;
    private final String nodeUrl;
    private final Web3j web3j;
    private static final Logger log = LoggerFactory.getLogger(EthereumAdapter.class);


    public EthereumAdapter(final String nodeUrl) {
        this.nodeUrl = nodeUrl;
        this.web3j = Web3j.build(new HttpService(this.nodeUrl));
    }


    public boolean testConnectionToNode() {
        try {
            log.info("Connected to Ethereum client: URL: {}, Version: {}", this.nodeUrl, this.web3j.web3ClientVersion().send().getWeb3ClientVersion());
            return true;
        } catch (IOException e) {
            log.error("Failed to connect to Ethereum client at URL: {}. Reason: {}", this.nodeUrl, e.getMessage());

            return false;
        }
    }


    public void setCredentials(String password, String fileSource) throws IOException, CipherException {
        try {
            this.credentials = WalletUtils.loadCredentials(password, fileSource);
        } catch (IOException | CipherException e) {
            log.error("Error occurred while setting the user credentials for Ethereum. Reason {}", e.getMessage());
            throw e;
        }
    }

    /**
     * Subscribes for the event of detecting a transition of the state of a given transaction which is assumed to having been
     * MINED before. The method supports detecting
     * (i) a transaction being not found anymore (invalidated), or (ii) not having a containing block (orphaned), or
     * (iii) having received enough block-confirmations (durably committed).
     *
     * @param txHash         the hash of the transaction to monitor
     * @param waitFor        the number of block-confirmations to wait until the transaction is considered persisted (-1 if the
     *                       transaction is never to be considered persisted)
     * @param observedStates the set of states that will be reported to the calling method
     * @return a future which is used to handle the subscription and receive the callback
     */
    private CompletableFuture<Transaction> subscribeForTxEvent(String txHash, long waitFor, TransactionState... observedStates) {
        final CompletableFuture<Transaction> result = new CompletableFuture<>();
        final Subscription subscription = web3j.blockObservable(false).subscribe(new Subscriber<EthBlock>() {

            void handleDetectedState(final Optional<org.web3j.protocol.core.methods.response.Transaction> transactionDetails,
                                     final TransactionState detectedState, final TransactionState[] interesting,
                                     CompletableFuture<Transaction> future) {
                // Only complete the future if we are interested in this event
                if (Arrays.asList(interesting).contains(detectedState)) {
                    final Transaction result = new Transaction();
                    result.setState(detectedState);

                    if (transactionDetails.isPresent()) {
                        result.setBlock(new Block(transactionDetails.get().getBlockNumber(), transactionDetails.get().getBlockHash()));
                        result.setFrom(transactionDetails.get().getFrom());
                        result.setTo(transactionDetails.get().getTo());
                        result.setTransactionHash(transactionDetails.get().getHash());
                        result.setValue(transactionDetails.get().getValue());
                    }
                    future.complete(result);

                }

            }

            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable throwable) {
                result.completeExceptionally(throwable);
            }

            @Override
            public void onNext(EthBlock ethBlock) {
                try {
                    // make sure the transaction exists
                    final EthTransaction transaction = web3j.ethGetTransactionByHash(txHash).send();
                    // if not, then it is either invalidated or did not exist in the first place
                    if (!transaction.getTransaction().isPresent()) {
                        final String msg = String.format("The transaction of the hash %s is not found!", txHash);
                        log.info(msg);
                        handleDetectedState(transaction.getTransaction(), TransactionState.NOT_FOUND, observedStates, result);

                        return;

                    }

                    // make sure the transaction is still contained in a block, i.e., it was not orphaned
                    final String retrievedBlockHash = transaction.getTransaction().get().getBlockHash();

                    if (retrievedBlockHash == null || retrievedBlockHash.isEmpty()) {
                        final String msg = String.format("The transaction of the hash %s has no block (orphaned?)",
                                txHash);
                        log.info(msg);

                        handleDetectedState(transaction.getTransaction(), TransactionState.PENDING, observedStates, result);
                        return;

                    }
                    // check if enough block-confirmations have occurred.
                    if (waitFor >= 0 && ethBlock.getBlock() != null) {
                        if (ethBlock.getBlock().getNumber()
                                .subtract(transaction.getTransaction().get().getBlockNumber())
                                .intValue() >= waitFor) {
                            final String msg = String.format("The transaction of the hash %s has been confirmed",
                                    txHash);
                            log.info(msg);

                            handleDetectedState(transaction.getTransaction(), TransactionState.CONFIRMED, observedStates, result);
                        }
                    }

                } catch (IOException e) {
                    result.completeExceptionally(e);
                }
            }
        });
        //unsubscribe the observable when the CompletableFuture completes (either when detecting an event, or manually)
        result.whenComplete((v, e) -> subscription.unsubscribe());

        return result;
    }


    private static CompletionException wrapEthereumExceptions(Throwable e) {
        if (e.getCause() instanceof IOException)
            e = new BlockchainNodeUnreachableException(e);
        else if (e.getCause() instanceof RuntimeException)
            e = new InvalidTransactionException(e);

        return new CompletionException(e);
    }

    @Override
    public CompletableFuture<Transaction> submitTransaction(long waitFor, String receiverAddress, BigDecimal value)
            throws InvalidTransactionException {
        if (credentials == null) {
            log.error("Credentials are not set for the Ethereum user");
            throw new NullPointerException("Credentials are not set for the Ethereum user");
        }

        try {
            return Transfer.sendFunds(web3j, credentials, receiverAddress, value, Convert.Unit.WEI)  // 1 wei = 10^-18 Ether
                    .sendAsync()
                    // when an exception (e.g., ConnectException happens), the following is skipped
                    .thenCompose(tx -> subscribeForTxEvent(tx.getTransactionHash(), waitFor, TransactionState.CONFIRMED, TransactionState.NOT_FOUND))
                    .exceptionally((e) -> {
                                throw wrapEthereumExceptions(e);
                            }
                    );

        } catch (Exception e) {// this seems to never get invoked
            final String msg = "An error occurred while trying to submit a new transaction to ethereum. Reason: " + e.getMessage();
            log.error(msg);

            throw new InvalidTransactionException(msg, e);
        }


    }

    @Override
    public Observable<Transaction> receiveTransactions(long waitFor, String senderId) {
        if (credentials == null) {
            log.error("Credentials are not set for the Ethereum user");
            throw new NullPointerException("Credentials are not set for the Ethereum user");
        }

        final String myAddress = credentials.getAddress();
        final PublishSubject<Transaction> result = PublishSubject.create();
        final Subscription newTransactionObservable = web3j.transactionObservable().subscribe(tx -> {
            if (myAddress.equalsIgnoreCase(tx.getTo())) {
                if (senderId == null || senderId.trim().length() == 0 || senderId.equalsIgnoreCase(tx.getFrom())) {
                    log.info("New transaction received from:" + tx.getFrom());
                    subscribeForTxEvent(tx.getHash(), waitFor, TransactionState.CONFIRMED)
                            .thenAccept(result::onNext)
                            .exceptionally(error -> {
                                result.onError(wrapEthereumExceptions(error));
                                return null;
                            });
                }
            }
        }, e->result.onError(wrapEthereumExceptions(e)));

        return result.doOnUnsubscribe(newTransactionObservable::unsubscribe);
    }

    @Override
    public CompletableFuture<TransactionState> ensureTransactionState(long waitFor, String transactionId) {
        // only monitor the transition into the CONFIRMED state or the NOT_FOUND state
        return subscribeForTxEvent(transactionId, waitFor, TransactionState.CONFIRMED, TransactionState.NOT_FOUND)
                .thenApply(Transaction::getState)
                .exceptionally((e) -> {
                    throw wrapEthereumExceptions(e);
                });
    }

    @Override
    public CompletableFuture<TransactionState> detectOrphanedTransaction(String transactionId) {
        // only monitor the transition into the PENDING state
        return subscribeForTxEvent(transactionId, -1, TransactionState.PENDING, TransactionState.NOT_FOUND)
                .thenApply(Transaction::getState)
                .exceptionally((e) -> {
                    throw wrapEthereumExceptions(e);
                });
    }


}
