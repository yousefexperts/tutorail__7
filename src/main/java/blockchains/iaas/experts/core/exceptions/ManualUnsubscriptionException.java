package blockchains.iaas.experts.core.exceptions;


public class ManualUnsubscriptionException extends RuntimeException {
    public ManualUnsubscriptionException() {
        super();
    }

    public ManualUnsubscriptionException(String message) {
        super(message);
    }

    public ManualUnsubscriptionException(String message, Throwable cause) {
        super(message, cause);
    }

    public ManualUnsubscriptionException(Throwable cause) {
        super(cause);
    }

    protected ManualUnsubscriptionException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
