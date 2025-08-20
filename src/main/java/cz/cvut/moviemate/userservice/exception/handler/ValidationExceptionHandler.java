package cz.cvut.moviemate.userservice.exception.handler;

import cz.cvut.moviemate.userservice.dto.error.ApiErrorMultipleResponses;
import cz.cvut.moviemate.userservice.dto.error.ApiErrorSingleResponse;
import cz.cvut.moviemate.userservice.exception.DuplicateException;
import cz.cvut.moviemate.userservice.exception.InvalidTimestampException;
import cz.cvut.moviemate.userservice.exception.NotFoundException;
import cz.cvut.moviemate.userservice.util.ApiErrorFactory;
import lombok.RequiredArgsConstructor;
import org.apache.coyote.BadRequestException;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.method.annotation.HandlerMethodValidationException;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

@ControllerAdvice
@RequiredArgsConstructor
@Order(1)
public class ValidationExceptionHandler {

    @ExceptionHandler(BadRequestException.class)
    @ResponseBody
    public ResponseEntity<ApiErrorSingleResponse> handleBadRequestException(BadRequestException ex, WebRequest request) {
        HttpStatus status = HttpStatus.BAD_REQUEST;

        ApiErrorSingleResponse response = ApiErrorFactory.createApiErrorResponse(
                status,
                "BAD_REQUEST",
                ex.getMessage(),
                "The request cannot be processed due to invalid input.",
                request,
                ex.getCause(),
                new String[]{"Check the request format and try again.", "Refer to API documentation."}
        );

        return new ResponseEntity<>(response, status);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseBody
    public ResponseEntity<ApiErrorMultipleResponses> handleArgumentNotValidException(MethodArgumentNotValidException ex, WebRequest request) {
        HttpStatus status = HttpStatus.BAD_REQUEST;
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach(error -> {
            FieldError fieldError = (FieldError) error;
            errors.put(fieldError.getField(), fieldError.getDefaultMessage());
        });

        ApiErrorMultipleResponses errorResponse = ApiErrorFactory.createApiErrorResponses(
                status,
                "ARGUMENT_FORMAT_ERROR",
                errors,
                new String[]{"Refer to the error details for more information.", "Correct the input data and try again."},
                ex.getCause(),
                request
        );

        return new ResponseEntity<>(errorResponse, status);
    }

    @ExceptionHandler(HandlerMethodValidationException.class)
    @ResponseBody
    public ResponseEntity<ApiErrorMultipleResponses> handleMethodNotValidException(HandlerMethodValidationException ex, WebRequest request) {
        HttpStatus status = HttpStatus.BAD_REQUEST;
        Map<String, String> errors = new HashMap<>();
        AtomicInteger index = new AtomicInteger(1);
        ex.getAllErrors().forEach(error -> {
            String fieldName = "error" + index.getAndIncrement();
            errors.put(fieldName, error.getDefaultMessage());
        });

        ApiErrorMultipleResponses errorResponse = ApiErrorFactory.createApiErrorResponses(
                status,
                "METHOD_VALIDATION_ERROR",
                errors,
                new String[]{"Ensure that the method arguments meet the required criteria.", "Review the error messages for more details."},
                ex.getCause(),
                request
        );

        return new ResponseEntity<>(errorResponse, status);
    }

    @ExceptionHandler(DuplicateException.class)
    @ResponseBody
    public ResponseEntity<ApiErrorSingleResponse> handleDuplicateException(DuplicateException ex, WebRequest request) {
        HttpStatus status = HttpStatus.CONFLICT;

        ApiErrorSingleResponse errorResponse = ApiErrorFactory.createApiErrorResponse(
                status,
                ex.getErrorType(),
                ex.getMessage(),
                "The request could not be completed due to a conflict with the current state of the resource.",
                request,
                ex.getCause(),
                new String[]{"Check for duplicate data and try again.", "Ensure that the resource is in a valid state."}
        );

        return new ResponseEntity<>(errorResponse, status);
    }

    @ExceptionHandler(NotFoundException.class)
    @ResponseBody
    public ResponseEntity<ApiErrorSingleResponse> handleNotFoundException(NotFoundException ex, WebRequest request) {
        HttpStatus status = HttpStatus.NOT_FOUND;

        ApiErrorSingleResponse errorResponse = ApiErrorFactory.createApiErrorResponse(
                status,
                ex.getErrorType(),
                ex.getMessage(),
                "Resource with specified identifier did not found.",
                request,
                ex.getCause(),
                new String[]{"Check resource identifier."}
        );

        return new ResponseEntity<>(errorResponse, status);
    }

    @ExceptionHandler(InvalidTimestampException.class)
    @ResponseBody
    public ResponseEntity<ApiErrorSingleResponse> handleInvalidTimestampException(InvalidTimestampException ex, WebRequest request) {
        HttpStatus status = HttpStatus.BAD_REQUEST;

        ApiErrorSingleResponse errorResponse = ApiErrorFactory.createApiErrorResponse(
                status,
                ex.getErrorType(),
                ex.getMessage(),
                "The timestamp format or date range must be wrong.",
                request,
                ex.getCause(),
                new String[]{"Check the timestamp format.", "Validate date range."}
        );

        return new ResponseEntity<>(errorResponse, status);
    }
}
