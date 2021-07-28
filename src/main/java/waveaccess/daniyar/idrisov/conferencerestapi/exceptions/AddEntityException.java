package waveaccess.daniyar.idrisov.conferencerestapi.exceptions;

public class AddEntityException extends RuntimeException{

    public AddEntityException() {
        super();
    }

    public AddEntityException(String message) {
        super(message);
    }

    public AddEntityException(String message, Throwable cause) {
        super(message, cause);
    }

    public AddEntityException(Throwable cause) {
        super(cause);
    }

    protected AddEntityException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }

}
