package blockchains.iaas.experts.core.adaptation.interfaces;

import blockchains.iaas.experts.core.exceptions.InvalidTransactionException;
import blockchains.iaas.experts.core.model.Transaction;
import blockchains.iaas.experts.core.model.TransactionState;
import rx.Observable;


import java.io.IOException;
import java.math.BigDecimal;
import java.util.concurrent.CompletableFuture;


public interface BlockchainAdapter {
    
    CompletableFuture<Transaction> submitTransaction(long waitFor, String receiverAddress, BigDecimal value) throws InvalidTransactionException;

    /**
     * receives transactions addressed to us (potentially from a specific sender)
     * @param waitFor the number of block-confirmations to be detected before emitting a result
     * @param senderId an optional address of the sender. If specified, only transactions from this sender are considered
     * @return an observable that emits a summary of the received transaction whenever one is detected
     */
    Observable<Transaction> receiveTransactions(long waitFor, String senderId);


    /**
     * ensures that a transaction receives enough block-confirmations
     * @param waitFor the number of block-confirmations to be detected before considering the transaction to be confirmed
     * @param transactionId the hash of the transaction we want to monitor
     * @return a completable future that emits the new state of the transaction (either COFIRMED in case the desired
     * number of block-confirmations got received, or NOT_FOUND if the transaction got invalidated).
     * The future should exceptionally complete with an exception of type BlockchainNodeUnreachableException if the blockchain node is not reachable
     */
    CompletableFuture<TransactionState> ensureTransactionState(long waitFor, String transactionId);


    /**
     * detects that the given transaction got orphaned
     * @param transactionId the hash of the transaction we want to monitor
     * @return a completable future that emits the new state of the transaction (PENDING meaning that it no longer has a
     * block, i.e., it is orphaned)
     * The future should exceptionally complete with an exception of type BlockchainNodeUnreachableException if the blockchain node is not reachable
     */
    CompletableFuture<TransactionState> detectOrphanedTransaction(String transactionId);

}
