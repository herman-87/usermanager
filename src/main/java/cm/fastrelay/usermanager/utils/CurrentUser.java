package cm.fastrelay.usermanager.utils;

import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.context.SecurityContextHolder;

@Slf4j
public class CurrentUser {

    public static String getUserName() {
        String currentUsername = SecurityContextHolder.getContext().getAuthentication().getName();
        if (currentUsername == null) {
            log.error("The Current Username Is Null");
            throw new RuntimeException("The current username is null");
        }
        return currentUsername;
    }
}
