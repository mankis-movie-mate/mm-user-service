package cz.cvut.moviemate.userservice.exception.handler;

import cz.cvut.moviemate.userservice.exception.MovieMateBaseException;
import cz.cvut.moviemate.userservice.exception.RestrictException;
import cz.cvut.moviemate.userservice.dto.error.ApiErrorSingleResponse;
import cz.cvut.moviemate.userservice.util.ApiErrorFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.context.request.WebRequest;

@ControllerAdvice
@RequiredArgsConstructor
@Order
public class GlobalExceptionHandler {

    @ExceptionHandler(Exception.class)
    @ResponseBody
    public ResponseEntity<ApiErrorSingleResponse> handleGenericException(Exception ex, WebRequest request) {
        HttpStatus status = HttpStatus.INTERNAL_SERVER_ERROR;

        ApiErrorSingleResponse errorResponse = ApiErrorFactory.createApiErrorResponse(
                status,
                "INTERNAL_SERVER_ERROR",
                String.format("Internal error: %s", ex.getMessage()),
                "An unexpected error occurred. Please try again later.",
                request,
                ex.getCause(),
                new String[]{"Check the application logs for more details.", "Contact support if the issue persists."}
        );

        return new ResponseEntity<>(errorResponse, status);
    }

    @ExceptionHandler(MovieMateBaseException.class)
    @ResponseBody
    public ResponseEntity<ApiErrorSingleResponse> handleAppException(MovieMateBaseException ex, WebRequest request) {
        HttpStatus status = HttpStatus.I_AM_A_TEAPOT;

        ApiErrorSingleResponse errorResponse = ApiErrorFactory.createApiErrorResponse(
                status,
                ex.getErrorType(),
                String.format("Generic internal error: %s", ex.getMessage()),
                "Service unavailable due to generic internal error.",
                request,
                ex.getCause(),
                new String[]{"Check application logs for more details."}
        );

        return new ResponseEntity<>(errorResponse, status);
    }

    @ExceptionHandler(RestrictException.class)
    @ResponseBody
    public ResponseEntity<ApiErrorSingleResponse> handleRestrictException(RestrictException ex, WebRequest request) {
        HttpStatus status = HttpStatus.FORBIDDEN;

        ApiErrorSingleResponse errorResponse = ApiErrorFactory.createApiErrorResponse(
                status,
                ex.getErrorType(),
                ex.getMessage(),
                "This action is not allowed for this user.",
                request,
                ex.getCause(),
                new String[]{"Please contact the system administrator for further information."}
        );

        return new ResponseEntity<>(errorResponse, status);
    }

    @ExceptionHandler(AccessDeniedException.class)
    @ResponseBody
    public ResponseEntity<ApiErrorSingleResponse> handleAccessDenied(AccessDeniedException ex, WebRequest request) {
        HttpStatus status = HttpStatus.FORBIDDEN;

        ApiErrorSingleResponse errorResponse = ApiErrorFactory.createApiErrorResponse(
                status,
                "ACCESS_DENIED",
                ex.getMessage(),
                "Access is restricted. Please check your permissions or contact support.",
                request,
                ex.getCause(),
                new String[]{"Ensure you have the necessary permissions.", "Contact support if you believe this is an error."}
        );

        return new ResponseEntity<>(errorResponse, status);
    }
}
