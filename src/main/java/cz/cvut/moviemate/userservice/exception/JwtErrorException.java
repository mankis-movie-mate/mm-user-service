package cz.cvut.moviemate.userservice.exception;

public class JwtErrorException extends MovieMateBaseException {

    public JwtErrorException(String message) {
        super(message, "JWT_ERROR");
    }
}
