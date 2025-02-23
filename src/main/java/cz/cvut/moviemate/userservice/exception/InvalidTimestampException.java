package cz.cvut.moviemate.userservice.exception;

public class InvalidTimestampException extends MovieMateBaseException {

    public InvalidTimestampException(String message) {
        super(message, "TIMESTAMP_VALIDATION_ERROR");
    }
}
