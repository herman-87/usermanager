package cm.fastrelay.usermanager.exception;

public class InternalServerError extends RuntimeException{
    public InternalServerError(String errorMessage) {
        super(errorMessage);
    }

}
