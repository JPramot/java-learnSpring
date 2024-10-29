package dev.lpa.goutbackend.commons.exceptions;

public class EntityNotFound extends RuntimeException {

    public EntityNotFound() {
        this(null);
    }

    public EntityNotFound(String message) {
        super(message);
    }
}
