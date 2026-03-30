package go.seni.java.util;

import liquibase.repackaged.org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpHeaders;

import javax.servlet.http.HttpServletRequest;
import java.util.Optional;

public class TokenUtils {

    private static final int BEARER_PREFIX_LENGTH = 7; // length of "Bearer "

    public static Optional<String> extractBearerToken(HttpServletRequest request) {
        String header = request.getHeader(HttpHeaders.AUTHORIZATION);
        if (StringUtils.isBlank(header) || header.length() <= BEARER_PREFIX_LENGTH) {
            return Optional.empty();
        }
        if (!header.startsWith("Bearer ")) {
            return Optional.empty();
        }
        String token = header.substring(BEARER_PREFIX_LENGTH);
        return Optional.of(token);
    }
}