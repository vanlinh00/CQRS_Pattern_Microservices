package go.seni.java.security;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import go.seni.java.entity.OAuthLoginRequest;
import go.seni.java.util.TokenUtils;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerEndpointsConfiguration;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.endpoint.TokenEndpoint;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.HttpRequestMethodNotSupportedException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.security.Principal;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;



@Service
@Transactional
public class AdminOauthService {

    private final HttpServletRequest httpServletRequest;
    private final AuthorizationServerEndpointsConfiguration authorizationServerEndpointsConfiguration;

    private final TokenStore tokenStore;
   // private final DeviceTokenRepository deviceTokenRepository;

    public AdminOauthService(HttpServletRequest httpServletRequest, AuthorizationServerEndpointsConfiguration authorizationServerEndpointsConfiguration, TokenStore tokenStore) {
        this.httpServletRequest = httpServletRequest;
        this.authorizationServerEndpointsConfiguration = authorizationServerEndpointsConfiguration;
        this.tokenStore = tokenStore;
    }

    public OAuth2AccessToken getToken(OAuthLoginRequest request) throws HttpRequestMethodNotSupportedException {
        Principal clientPrincipal = httpServletRequest.getUserPrincipal();

        ObjectMapper objectMapper = new ObjectMapper();
        Map<String, String> parameters = objectMapper.convertValue(request
                , new TypeReference<Map<String, String>>() {
                });

        for (Map.Entry<String, String> entry : parameters.entrySet()) {
            httpServletRequest.setAttribute(entry.getKey(), entry.getValue());
        }

        TokenEndpoint endpoint = new TokenEndpoint();
        endpoint.setClientDetailsService(authorizationServerEndpointsConfiguration.getEndpointsConfigurer().getClientDetailsService());
        endpoint.setOAuth2RequestFactory(authorizationServerEndpointsConfiguration.getEndpointsConfigurer().getOAuth2RequestFactory());
        endpoint.setTokenGranter(authorizationServerEndpointsConfiguration.getEndpointsConfigurer().getTokenGranter());
        return endpoint.postAccessToken(clientPrincipal, parameters).getBody();
    }



    @Transactional
    public void logout(HttpServletRequest request) {

        Optional<String> tokenOpt = TokenUtils.extractBearerToken(request);
        if (!tokenOpt.isPresent()) {
            return;
        }
        String token = tokenOpt.get();
        OAuth2AccessToken accessToken = tokenStore.readAccessToken(token);
        if (Objects.isNull(accessToken)) {
            return;
        }

       //this.deleteDeviceToken(accessToken);

        tokenStore.removeAccessToken(accessToken);
        if (accessToken.getRefreshToken() != null) {
            tokenStore.removeRefreshToken(accessToken.getRefreshToken());
        }
    }

//    private void deleteDeviceToken(OAuth2AccessToken oAuth2AccessToken) {
//        if (Objects.nonNull(oAuth2AccessToken)) {
//            OAuth2Authentication auth = tokenStore.readAuthentication(oAuth2AccessToken.getValue());
//            Long userId = Optional.ofNullable(auth)
//                    .map(OAuth2Authentication::getUserAuthentication)
//                    .map(Authentication::getPrincipal)
//                    .filter(p -> p instanceof PrincipalAdminDetail)
//                    .map(p -> (PrincipalAdminDetail) p)
//                    .map(PrincipalAdminDetail::getUser)
//                    .map(User::getId)
//                    .orElse(0L);
//
//            String jti = StringUtils.defaultString((String) oAuth2AccessToken.getAdditionalInformation().get("jti"));
//            deviceTokenRepository.deleteByJtiAndUserId(jti, userId);
//        }
//    }
}
