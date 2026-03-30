package go.seni.java.util;

import go.seni.java.dto.CustomUserDetails;
import go.seni.java.entity.User;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;

public final class SecurityUtils {

    private SecurityUtils() {}

    public static Authentication currentAuthentication() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null || !auth.isAuthenticated() || auth instanceof AnonymousAuthenticationToken) {
            throw new RuntimeException("Unauthenticated");
        }
        return auth;
    }
//
//    public static String currentLoginId() {
//        Authentication auth = currentAuthentication();
//        Object principal = auth.getPrincipal();
//
//        if (principal instanceof User) {
//            return ((CustomUserDetails) principal).getUsername(); // loginId
//        }
//        if (principal instanceof User) {
//            return ((UserDetails) principal).getUsername();
//        }
//        return auth.getName();
//    }

    public static Long currentUserId() {
        Authentication auth = currentAuthentication();
        Object principal = auth.getPrincipal();

        if (principal instanceof User) {
            return ((User) principal).getId();
        }
        throw new RuntimeException("Current principal does not contain userId");
    }
}