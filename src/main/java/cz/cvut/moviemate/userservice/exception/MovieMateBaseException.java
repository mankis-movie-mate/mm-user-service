package cz.cvut.moviemate.userservice.exception;

import lombok.Getter;

@Getter
public class MovieMateBaseException extends RuntimeException {
    private final String errorType;

    public MovieMateBaseException(String message) {
        super(message);
        this.errorType = "GENERIC_ERROR";
    }

    public MovieMateBaseException(String message, String errorType) {
        super(message);
        this.errorType = errorType;
    }

    public MovieMateBaseException(Throwable cause, String errorType) {
        super(cause);
        this.errorType = errorType;
    }

    public MovieMateBaseException(String message, Throwable cause, String errorType) {
        super(message, cause);
        this.errorType = errorType;
    }
}
