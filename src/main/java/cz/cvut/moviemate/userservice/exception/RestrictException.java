package cz.cvut.moviemate.userservice.exception;

public class RestrictException extends MovieMateBaseException {

    public RestrictException(String message) {
        super(message, "RESTRICT_EXCEPTION");
    }
}
