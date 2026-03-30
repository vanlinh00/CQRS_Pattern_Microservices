package go.seni.java.services;

import org.springframework.security.core.AuthenticationException;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.OAuth2Request;
import org.springframework.security.oauth2.provider.token.DefaultTokenServices;

import javax.servlet.http.HttpServletRequest;
import java.util.Objects;

/**
 * @author PhuocVD
 */
public class CustomTokenServices extends DefaultTokenServices {
   // private final LoginHistoryRepository loginHistoryRepository;
   // private final HttpServletRequest httpServletRequest;

//    public CustomTokenServices(LoginHistoryRepository loginHistoryRepository, HttpServletRequest httpServletRequest) {
//        this.httpServletRequest = httpServletRequest;
//        this.setReuseRefreshToken(false);
//        this.loginHistoryRepository = loginHistoryRepository;
//    }

//    @Override
//    public OAuth2AccessToken createAccessToken(OAuth2Authentication authentication) throws AuthenticationException {
//        PrincipalAdminDetail principalAdminDetail = (PrincipalAdminDetail) authentication.getPrincipal();
//        User user = principalAdminDetail.getUser();
//        String ipAddress = BaseUtils.getIpAddress(httpServletRequest);
//        OAuth2AccessToken auth2AccessToken = super.createAccessToken(authentication);
//        this.createLoginHistory(user, ipAddress, true, BaseUtils.getErrorMessageDefault(MessageCode.SUCCESS));
//        return auth2AccessToken;
//    }

    @Override
    protected int getAccessTokenValiditySeconds(OAuth2Request clientAuth) {
        return super.getAccessTokenValiditySeconds(clientAuth);
    }

//    private void createLoginHistory(User user, String ipAddress, boolean success, String message) {
//        if (Objects.nonNull(user)) {
//            LoginHistory loginHistory = new LoginHistory();
//            loginHistory.setUserId(user.getId());
//            loginHistory.setIpAddress(ipAddress);
//            loginHistory.setSuccess(success);
//            loginHistory.setMessage(message);
//            loginHistoryRepository.save(loginHistory);
//        }
//    }
}
