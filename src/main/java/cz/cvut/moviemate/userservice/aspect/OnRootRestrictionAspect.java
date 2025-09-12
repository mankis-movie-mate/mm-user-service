package cz.cvut.moviemate.userservice.aspect;

import cz.cvut.moviemate.userservice.exception.RestrictException;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Aspect
@Slf4j(topic = "ON_ROOT_RESTRICTION")
@Component
public class OnRootRestrictionAspect {

    @Value("${security.root-user.username}")
    private String rootUsername;


    @Before("@annotation(cz.cvut.moviemate.userservice.aspect.annotation.OnRootRestriction) && args(username,..)")
    public void checkRootUserRestriction(JoinPoint joinPoint, String username) {
        log.info("Comparing rootUsername='{}' with username='{}'", rootUsername, username);
        if (rootUsername != null && rootUsername.trim().equalsIgnoreCase(username.trim())) {
            log.warn("Operation not allowed for the root user in method: {}", joinPoint.getSignature().getName());
            throw new RestrictException("Operation not allowed for the root user.");
        }
    }
}
