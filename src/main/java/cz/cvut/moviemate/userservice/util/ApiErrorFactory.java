package cz.cvut.moviemate.userservice.util;

import cz.cvut.moviemate.userservice.dto.error.ApiErrorMultipleResponses;
import cz.cvut.moviemate.userservice.dto.error.ApiErrorSingleResponse;
import org.springframework.http.HttpStatus;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.context.request.WebRequest;

import java.time.LocalDateTime;
import java.util.Map;

public class ApiErrorFactory {

    private ApiErrorFactory() {
    }

    public static ApiErrorMultipleResponses createApiErrorResponses(
            HttpStatus status,
            String errorName,
            Map<String, String> errors,
            String[] suggestions,
            Throwable causedBy,
            WebRequest webRequest) {
        return ApiErrorMultipleResponses.builder()
                .errorCode(status.value())
                .errorName(errorName)
                .errorsMessages(errors)
                .timestamp(LocalDateTime.now())
                .causedBy(getErrorCause(causedBy))
                .suggestions(suggestions)
                .path(((ServletWebRequest) webRequest).getRequest().getRequestURI())
                .method(((ServletWebRequest) webRequest).getRequest().getMethod())
                .build();
    }

    public static ApiErrorSingleResponse createApiErrorResponse(
            HttpStatus status,
            String errorName,
            String message,
            String userMessage,
            WebRequest webRequest,
            Throwable causedBy,
            String[] suggestions) {

        return ApiErrorSingleResponse.builder()
                .errorCode(status.value())
                .errorName(errorName)
                .userMessage(userMessage)
                .timestamp(LocalDateTime.now())
                .path(((ServletWebRequest) webRequest).getRequest().getRequestURI())
                .method(((ServletWebRequest) webRequest).getRequest().getMethod())
                .causedBy(getErrorCause(causedBy))
                .suggestions(suggestions)
                .message(message)
                .build();
    }

    private static String getErrorCause(Throwable cause) {
        String causeClass = cause != null ? cause.getClass().getSimpleName() : "N/A";
        String causeMessage = cause != null ? cause.getMessage() : "null message from cause.";
        return causeClass.concat(", message: ").concat(causeMessage);
    }
}
