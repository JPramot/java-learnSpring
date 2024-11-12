package dev.lpa.goutbackend.commons.exceptions;

public class CredentialExistException extends RuntimeException {

    public CredentialExistException() {
        super();
    }

    public CredentialExistException(String message) {
        super(message);
    }
}
