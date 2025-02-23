package cz.cvut.moviemate.userservice.exception.handler;

import cz.cvut.moviemate.userservice.exception.JwtErrorException;
import cz.cvut.moviemate.userservice.exception.UserBannedException;
import cz.cvut.moviemate.userservice.exception.UserDeletedException;
import cz.cvut.moviemate.userservice.dto.error.ApiErrorSingleResponse;
import cz.cvut.moviemate.userservice.util.ApiErrorFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.context.request.WebRequest;

@ControllerAdvice
@RequiredArgsConstructor
@Order(1)
public class AuthExceptionHandler {

    @ExceptionHandler(UserBannedException.class)
    public ResponseEntity<ApiErrorSingleResponse> handleBannedException(UserBannedException ex, WebRequest request) {
        HttpStatus status = HttpStatus.LOCKED;

        ApiErrorSingleResponse errorResponse = ApiErrorFactory.createApiErrorResponse(
                status,
                ex.getErrorType(),
                "Banned or deleted user tried to access the app.",
                ex.getMessage(),
                request,
                ex.getCause(),
                new String[]{"Contact support for the further information."}
        );

        return new ResponseEntity<>(errorResponse, status);
    }

    @ExceptionHandler(UserDeletedException.class)
    public ResponseEntity<ApiErrorSingleResponse> handleDeletedException(UserDeletedException ex, WebRequest request) {
        HttpStatus status = HttpStatus.LOCKED;

        ApiErrorSingleResponse errorResponse = ApiErrorFactory.createApiErrorResponse(
                status,
                ex.getErrorType(),
                "Deleted user tried to access the app.",
                ex.getMessage(),
                request,
                ex.getCause(),
                new String[]{"Contact support for the further information."}
        );

        return new ResponseEntity<>(errorResponse, status);
    }

    @ExceptionHandler(JwtErrorException.class)
    @ResponseBody
    public ResponseEntity<ApiErrorSingleResponse> handleUnauthorizedException(JwtErrorException ex, WebRequest request) {
        HttpStatus status = HttpStatus.BAD_REQUEST;

        ApiErrorSingleResponse errorResponse = ApiErrorFactory.createApiErrorResponse(
                status,
                ex.getErrorType(),
                ex.getMessage(),
                "Token was expired or malformed.",
                request,
                ex.getCause(),
                new String[]{"Check the token format or refresh token."}
        );

        return new ResponseEntity<>(errorResponse, status);
    }

    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<ApiErrorSingleResponse> handleBadCredentials(Exception ex, WebRequest request) {
        HttpStatus status = HttpStatus.UNAUTHORIZED;

        ApiErrorSingleResponse errorResponse = ApiErrorFactory.createApiErrorResponse(
                status,
                "BAD_CREDENTIALS",
                "Provided incorrect credentials.",
                ex.getMessage(),
                request,
                ex.getCause(),
                new String[]{"Verify your credentials and try again.", "Contact support if the problem persists."}
        );

        return new ResponseEntity<>(errorResponse, status);
    }
}
