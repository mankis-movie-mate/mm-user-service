package cz.cvut.moviemate.userservice.exception;

public class NotFoundException extends MovieMateBaseException {

    public NotFoundException(String message) {
        super(message, "NOT_FOUND_ERROR");
    }
}
