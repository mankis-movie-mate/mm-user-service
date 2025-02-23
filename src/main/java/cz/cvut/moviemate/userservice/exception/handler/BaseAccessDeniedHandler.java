package cz.cvut.moviemate.userservice.exception.handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import cz.cvut.moviemate.userservice.dto.error.ApiErrorSingleResponse;
import cz.cvut.moviemate.userservice.util.ApiErrorFactory;
import cz.cvut.moviemate.userservice.util.PrincipalUtil;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.ServletWebRequest;

import java.io.IOException;

@Component
@Slf4j(topic = "ACCESS_HANDLER")
@RequiredArgsConstructor
public class BaseAccessDeniedHandler implements AccessDeniedHandler {

    private final ObjectMapper objectMapper;
    private final PrincipalUtil principalUtil;

    private static final String ACCESS_DENIED_MESSAGE = "You do not have permission to access this resource.";
    private static final String ACCESS_DENIED_DETAILS = "Access is restricted. Please check your permissions or contact support.";
    private static final String[] RESOLUTION_SUGGESTIONS = {
            "Ensure you have the necessary permissions.",
            "Contact support if you believe this is an error."
    };

    @Override
    public void handle(
            HttpServletRequest request,
            HttpServletResponse response,
            AccessDeniedException accessDeniedException
    ) throws IOException {
        response.setStatus(HttpServletResponse.SC_FORBIDDEN);
        response.setContentType("application/json");

        HttpStatus status = HttpStatus.FORBIDDEN;
        String username = principalUtil.getPrincipalUsername();
        log.warn("User {} attempted to access the protected URL: {}", username, request.getRequestURI());

        ApiErrorSingleResponse errorResponse = ApiErrorFactory.createApiErrorResponse(
                status,
                "ACCESS_DENIED",
                ACCESS_DENIED_MESSAGE,
                ACCESS_DENIED_DETAILS,
                new ServletWebRequest(request),
                accessDeniedException.getCause(),
                RESOLUTION_SUGGESTIONS
        );

        String jsonResponse = objectMapper.writeValueAsString(errorResponse);
        response.getWriter().write(jsonResponse);
    }
}
