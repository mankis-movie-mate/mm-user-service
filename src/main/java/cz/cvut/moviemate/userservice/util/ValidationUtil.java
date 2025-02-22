package cz.cvut.moviemate.userservice.util;

import cz.cvut.moviemate.userservice.exception.DuplicateException;
import cz.cvut.moviemate.userservice.exception.NotFoundException;
import cz.cvut.moviemate.userservice.exception.UserBannedException;
import cz.cvut.moviemate.userservice.exception.UserDeletedException;
import cz.cvut.moviemate.userservice.model.AppUser;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.util.function.Function;

@Component
@Slf4j(topic = "USER_SERVICE_VALIDATION_UTIL")
public class ValidationUtil {

    public void checkUserBannedOrDeleted(UserDetails userDetails) {
        String username = userDetails.getUsername();

        if (!userDetails.isAccountNonLocked()) {
            log.warn("User {} with banned account tried to access the app.", username);
            throw new UserBannedException(String.format("Account with username %s is banned.", username));
        }

        if (!userDetails.isEnabled()) {
            log.warn("User {} with deleted account tried to access the app.", username);
            throw new UserDeletedException(String.format("Account with username %s is deleted.", username));
        }
    }

    public <T> void checkDuplicate(T value, Function<T, AppUser> findFunc, String fieldName) {
        try {
            findFunc.apply(value);
            throw new DuplicateException(String.format("User with %s %s already exists", fieldName, value));
        } catch (NotFoundException ignored) {
            // Duplicates not found
        }
    }
}
