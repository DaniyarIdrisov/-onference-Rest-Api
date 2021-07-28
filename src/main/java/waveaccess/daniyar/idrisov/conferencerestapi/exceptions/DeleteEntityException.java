package waveaccess.daniyar.idrisov.conferencerestapi.exceptions;

public class DeleteEntityException extends RuntimeException {

    public DeleteEntityException() {
        super();
    }

    public DeleteEntityException(String message) {
        super(message);
    }

    public DeleteEntityException(String message, Throwable cause) {
        super(message, cause);
    }

    public DeleteEntityException(Throwable cause) {
        super(cause);
    }

    protected DeleteEntityException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }

}
