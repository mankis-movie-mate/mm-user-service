package cz.cvut.moviemate.userservice.config.filter;

import cz.cvut.moviemate.userservice.exception.JwtErrorException;
import cz.cvut.moviemate.userservice.exception.UserBannedException;
import cz.cvut.moviemate.userservice.exception.UserDeletedException;
import io.jsonwebtoken.JwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.servlet.HandlerExceptionResolver;

import java.io.IOException;

@Component
@Slf4j(topic = "USER_SERVICE_FILTER_LEVEL_EXCEPTION_HANDLER")
@RequiredArgsConstructor
public class FilterLevelExceptionHandler extends OncePerRequestFilter {
    private final HandlerExceptionResolver handlerExceptionResolver;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        try {
            filterChain.doFilter(request, response);
        } catch (JwtException e) {
            handleJwtException(e, request, response);
        } catch (UserBannedException e) {
            handleUserBannedException(e, request, response);
        } catch (UserDeletedException e) {
            handleUserDeletedException(e, request, response);
        }
    }

    private void handleJwtException(JwtException e, HttpServletRequest request, HttpServletResponse response) {
        handlerExceptionResolver.resolveException(
                request,
                response,
                null,
                new JwtErrorException("Error while processing token: " + e.getMessage())
        );
    }

    private void handleUserBannedException(UserBannedException e, HttpServletRequest request, HttpServletResponse response) {
        handlerExceptionResolver.resolveException(
                request,
                response,
                null,
                new UserBannedException(e.getMessage())
        );
    }

    private void handleUserDeletedException(UserDeletedException e, HttpServletRequest request, HttpServletResponse response) {
        handlerExceptionResolver.resolveException(
                request,
                response,
                null,
                new UserDeletedException(e.getMessage())
        );
    }
}