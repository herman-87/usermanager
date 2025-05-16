package cm.fastrelay.usermanager.exception;

public class ConflictException extends RuntimeException{
    public ConflictException(String errorMessage) {
        super(errorMessage);
    }

}
