package cm.fastrelay.usermanager.exception;

public class InvalidEmailAddressException extends RuntimeException{
    public InvalidEmailAddressException(String errorMessage) {
        super(errorMessage);
    }

}
