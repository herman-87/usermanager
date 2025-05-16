package cm.fastrelay.usermanager.controllers;

import cm.fastrelay.usermanager.exception.ConflictException;
import cm.fastrelay.usermanager.exception.ExternalServiceCallException;
import cm.fastrelay.usermanager.exception.InternalServerError;
import cm.fastrelay.usermanager.exception.InvalidEmailAddressException;
import cm.fastrelay.usermanager.exception.ResourceNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class UsermanagerControllerAdvice {

    @ExceptionHandler({ExternalServiceCallException.class})
    public ResponseEntity<String> handlerExternalServiceCallException(ExternalServiceCallException ex) {
        log.error(ex.getMessage(), ex);
        return ResponseEntity.status(500).body("Internal Server Error. Please try again later.");
    }

    @ExceptionHandler({ResourceNotFoundException.class})
    public ResponseEntity<String> handlerResourceNotFoundException(ResourceNotFoundException ex) {
        log.error(ex.getMessage(), ex);
        return ResponseEntity.status(404).body(ex.getMessage());
    }

    @ExceptionHandler({InvalidEmailAddressException.class})
    public ResponseEntity<String> handlerInvalidEmailAddressException(InvalidEmailAddressException ex) {
        log.error(ex.getMessage(), ex);
        return ResponseEntity.status(400).body(ex.getMessage());
    }

    @ExceptionHandler({InternalServerError.class})
    public ResponseEntity<String> handlerInternalServerError(InternalServerError ex) {
        log.error(ex.getMessage(), ex);
        return ResponseEntity.status(500).body(ex.getMessage());
    }

    @ExceptionHandler({ConflictException.class})
    public ResponseEntity<String> handlerInternalServerError(ConflictException ex) {
        log.error(ex.getMessage(), ex);
        return ResponseEntity.status(500).body(ex.getMessage());
    }
}
