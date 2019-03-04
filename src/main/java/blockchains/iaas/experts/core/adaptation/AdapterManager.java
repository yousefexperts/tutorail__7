package blockchains.iaas.experts.core.adaptation;

import blockchains.iaas.experts.core.adaptation.interfaces.BlockchainAdapter;
import blockchains.iaas.experts.core.exceptions.BlockchainIdNotFoundException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.web3j.crypto.CipherException;


import java.io.IOException;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;


public class AdapterManager {
    private static final Logger log = LoggerFactory.getLogger(AdapterManager.class);
    private static final String DEFAULT_ETHEREUM_ID = "eth-0";
    private static final String DEFUALT_BICOIN_ID = "btc-0";
    private BlockchainAdapterFactory factory = new BlockchainAdapterFactory();
    private static AdapterManager instance = null;
    private final Map<String, BlockchainAdapter> map = Collections.synchronizedMap(new HashMap<>());

    private AdapterManager(){

    }

    public static AdapterManager getInstance(){
        if(instance == null){
            instance = new AdapterManager();
            instance.initialize();
        }

        return instance;
    }

    public BlockchainAdapter getAdapter(String blockchainId) throws BlockchainIdNotFoundException {
        if(map.containsKey(blockchainId)){
            return map.get(blockchainId);
        }else{
            final String msg = String.format("blockchain-id <%s> does not exist!", blockchainId);
            log.error(msg);
            throw new BlockchainIdNotFoundException(msg);
        }
    }

    private void initialize(){
        try {
            addDefaultEthereumNode(DEFAULT_ETHEREUM_ID);
            addDefaultBitcoinNode(DEFUALT_BICOIN_ID);
        } catch (Exception e) {
            //TODO better handling of errors
            //error logs are produced at a lower level
            e.printStackTrace();
        }
    }

    private void addDefaultEthereumNode(String blockchainId) throws Exception {
        map.put(blockchainId, factory.createBlockchainAdapter(NodeType.ETHEREUM));
    }

    private void addDefaultBitcoinNode(String blockchainId) throws Exception {
        map.put(blockchainId, factory.createBlockchainAdapter(NodeType.BITCOIN));
    }

    // TODO provide ability to add/remove/configure nodes externally
    // TODO separate keystore management from node management
    // TODO expose nodes as a REST resource
}
