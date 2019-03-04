package blockchains.iaas.experts.core.adaptation;

import blockchains.iaas.experts.core.adaptation.adapters.BitcoinAdapter;
import blockchains.iaas.experts.core.adaptation.adapters.EthereumAdapter;
import blockchains.iaas.experts.core.adaptation.interfaces.BlockchainAdapter;
import blockchains.iaas.experts.core.config.Configuration;

import com.neemre.btcdcli4j.core.BitcoindException;
import com.neemre.btcdcli4j.core.CommunicationException;
import com.neemre.btcdcli4j.core.client.BtcdClient;
import com.neemre.btcdcli4j.core.client.BtcdClientImpl;
import com.neemre.btcdcli4j.daemon.BtcdDaemon;
import com.neemre.btcdcli4j.daemon.BtcdDaemonImpl;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.web3j.crypto.CipherException;

import java.io.IOException;


public class BlockchainAdapterFactory {

    private static final Logger log = LoggerFactory.getLogger(BlockchainAdapterFactory.class);

    public BlockchainAdapter createBlockchainAdapter(NodeType nodeType) throws Exception {
        try {
            switch (nodeType) {
                case ETHEREUM:
                    return createEthereumAdapter();
                case BITCOIN:
                    return createBitcoinAdapter();
                default:
                    log.error("Invalid node type!");
                    return null;
            }
        } catch (Exception e) {
            final String msg = String.format("Error while creating a blockchain adapter for %s. Details: %s", nodeType, e.getMessage());
            log.error(msg);
            throw new Exception(msg, e);
        }

    }

    private EthereumAdapter createEthereumAdapter() throws IOException, CipherException {
        final EthereumAdapter result = new EthereumAdapter(Configuration.getInstance().properties.getProperty("ethereum-node-url"));
        result.setCredentials(Configuration.getInstance().properties.getProperty("ethereum-keystore-password"),
                Configuration.getInstance().properties.getProperty("ethereum-keystore-path"));

        return result;

    }

    private BitcoinAdapter createBitcoinAdapter() throws BitcoindException, CommunicationException {
        final PoolingHttpClientConnectionManager connManager = new PoolingHttpClientConnectionManager();
        final CloseableHttpClient httpProvider = HttpClients.custom().setConnectionManager(connManager)
                .build();

        final BtcdClient client = new BtcdClientImpl(httpProvider, Configuration.getInstance().properties);
        final BtcdDaemon daemon = new BtcdDaemonImpl(client);

        return new BitcoinAdapter(client, daemon);
    }
}
