package cz.cvut.moviemate.userservice.dto.error;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.Map;

@Data
@Builder
public class ApiErrorMultipleResponses {
    private final Integer errorCode;
    private final String errorName;
    private final LocalDateTime timestamp;
    private final Map<String, String> errorsMessages;
    private final String path;
    private final String method;
    private final String causedBy;
    private final String[] suggestions;
}
