package blockchains.iaas.experts.core.exceptions;


public class BlockchainIdNotFoundException extends RuntimeException {
    public BlockchainIdNotFoundException() {
        super();
    }

    public BlockchainIdNotFoundException(String message) {
        super(message);
    }

    public BlockchainIdNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }

    public BlockchainIdNotFoundException(Throwable cause) {
        super(cause);
    }

    protected BlockchainIdNotFoundException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
