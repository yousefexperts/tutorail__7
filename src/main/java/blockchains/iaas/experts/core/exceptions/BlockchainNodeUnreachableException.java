package blockchains.iaas.experts.core.exceptions;


public class BlockchainNodeUnreachableException extends RuntimeException {
    public BlockchainNodeUnreachableException() {
    }

    public BlockchainNodeUnreachableException(String message) {
        super(message);
    }

    public BlockchainNodeUnreachableException(String message, Throwable cause) {
        super(message, cause);
    }

    public BlockchainNodeUnreachableException(Throwable cause) {
        super(cause);
    }

    public BlockchainNodeUnreachableException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
