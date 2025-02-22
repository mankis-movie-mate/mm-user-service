package cz.cvut.moviemate.userservice.dto.error;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class ApiErrorSingleResponse {
    private final Integer errorCode;
    private final String errorName;
    private final String message;
    private final String userMessage;
    private final LocalDateTime timestamp;
    private final String path;
    private final String method;
    private final String causedBy;
    private final String[] suggestions;
}
