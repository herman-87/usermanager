package cm.fastrelay.usermanager.exception;

public class ExternalServiceCallException extends RuntimeException{
    public ExternalServiceCallException(String errorMessage) {
        super(errorMessage);
    }

}
