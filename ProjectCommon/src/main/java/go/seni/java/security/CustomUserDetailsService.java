package go.seni.java.security;

import go.seni.java.constants.BaseConst;
import go.seni.java.dto.CustomUserDetails;
import go.seni.java.entity.User;
import go.seni.java.enums.MessageCode;
import go.seni.java.exception.BasicException;
import go.seni.java.heplers.TokenCredentialHelper;
import go.seni.java.repository.UserRepository;
import lombok.var;
import org.springframework.http.HttpHeaders;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.oauth2.common.exceptions.InvalidGrantException;
import org.springframework.security.web.authentication.preauth.PreAuthenticatedAuthenticationToken;
import org.springframework.security.core.userdetails.AuthenticationUserDetailsService;
import org.springframework.security.core.userdetails.UserDetailsService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.Collections;


@Slf4j
@Service("customUserDetailsService")
public class CustomUserDetailsService implements UserDetailsService, AuthenticationUserDetailsService<PreAuthenticatedAuthenticationToken> {

    private final HttpServletRequest httpServletRequest;
    private final UserRepository userRepository;

    public CustomUserDetailsService(HttpServletRequest httpServletRequest, UserRepository userRepository) {
        this.httpServletRequest = httpServletRequest;
        this.userRepository = userRepository;
    }


    @Override
    public UserDetails loadUserByUsername(String loginId) throws UsernameNotFoundException {
        String credentials = httpServletRequest.getHeader(HttpHeaders.AUTHORIZATION);
        String[] clientIdAndSecret = TokenCredentialHelper.parseCredentials(credentials);
        String clientId = clientIdAndSecret[0];
        log.info("*** loadUserByUsername {} with client_id = {}", loginId, clientId);

        if (BaseConst.MOBILE_CLIENT.equals(clientId)||BaseConst.ADMIN_CLIENT.equals(clientId)) {
            User user = userRepository.findFirstByLoginId(loginId)
                    .orElseThrow(
                        () ->    new BasicException(MessageCode.NOT_FOUND)
                        ///    () ->   new InvalidGrantException("User not found")
                    );
            //return new PrincipalUserDetail(user);
            var authorities = Collections.singletonList(new SimpleGrantedAuthority(user.getRole()));

            return new CustomUserDetails(
                    user.getId(),
                    user.getLoginId(),
                    user.getPasswordHash(),
                    authorities,
                    true
            );
        }

        throw new UsernameNotFoundException("User not found: " + loginId);
    }

    @Override
    public UserDetails loadUserDetails(PreAuthenticatedAuthenticationToken token) throws UsernameNotFoundException {
        Object principal = token.getPrincipal();
        log.info("Loading user details from PreAuthenticatedAuthenticationToken for principal: {}", principal);
        throw new UsernameNotFoundException("User not found for pre-authentication token: " + principal);
    }

}
