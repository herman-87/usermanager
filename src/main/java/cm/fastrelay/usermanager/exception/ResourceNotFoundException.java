package cm.fastrelay.usermanager.exception;

public class ResourceNotFoundException extends RuntimeException{
    public ResourceNotFoundException(String errorMessage) {
        super(errorMessage);
    }
}
