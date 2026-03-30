package go.seni.java.security;

import go.seni.java.dto.CustomUserDetails;
import go.seni.java.entity.User;
import go.seni.java.repository.UserRepository;
import go.seni.java.util.NumberUtil;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.common.exceptions.InvalidTokenException;
import org.springframework.security.oauth2.common.exceptions.UnauthorizedUserException;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.token.DefaultAccessTokenConverter;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.Map;
import java.util.Objects;

@Component
public class CustomAccessTokenConverter extends DefaultAccessTokenConverter {

    private static final String USER_ID = "user_id";
    private static final String USER_NAME = "user_name";
    private static final String NAME = "name";

    private final UserRepository userRepository;

    public CustomAccessTokenConverter(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public OAuth2Authentication extractAuthentication(Map<String, ?> claims) {
        OAuth2Authentication base = super.extractAuthentication(claims);
        base.setDetails(claims);

        Long userId = NumberUtil.toLong(claims.get(USER_ID));
        if (Objects.isNull(userId)) {
            throw new InvalidTokenException("Invalid token: missing user_id");
        }

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new InvalidTokenException("You do not have permission to access!"));


        if (Boolean.TRUE.equals(user.getIsDeleted())) {
//            log.warn("Blocked authentication: user is deleted. userId={}, loginId={}, claims={}",
//                    user.getId(), user.getLoginId(), claims);
            throw new InvalidTokenException("User has been deleted");
        }

        Authentication authentication =
                new UsernamePasswordAuthenticationToken(user, "N/A", base.getAuthorities());

        return new OAuth2Authentication(base.getOAuth2Request(), authentication);
    }

    @Override
    @SuppressWarnings("unchecked")
    public Map<String, ?> convertAccessToken(OAuth2AccessToken token, OAuth2Authentication authentication) {
        Map<String, Object> result = (Map<String, Object>) super.convertAccessToken(token, authentication);

        if (authentication != null && authentication.getPrincipal() instanceof CustomUserDetails) {
            CustomUserDetails customUserDetails = (CustomUserDetails) authentication.getPrincipal();

            result.put(USER_ID, customUserDetails.getId());
            result.put(USER_NAME, customUserDetails.getUsername()); // nếu User implements UserDetails -> loginId
           // result.put(NAME, customUserDetails.getFullName());
        }

        return result;
    }
}