package go.seni.java.security;

import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.OAuth2Request;
import org.springframework.security.oauth2.provider.token.DefaultAuthenticationKeyGenerator;

import java.util.LinkedHashMap;
import java.util.Map;


public class EnhancedAuthenticationKeyGenerator extends DefaultAuthenticationKeyGenerator {
    public static final String PARAM_DEVICE_TOKEN = "deviceToken";
    private static final String KEY_SUPER_KEY = "super_key";
    private static final String KEY_CLIENT_INSTANCE_ID = PARAM_DEVICE_TOKEN;

    @Override
    public String extractKey(final OAuth2Authentication authentication) {
        final String superKey = super.extractKey(authentication);

        final OAuth2Request authorizationRequest = authentication.getOAuth2Request();
        final Map<String, String> requestParameters = authorizationRequest.getRequestParameters();

        final String clientInstanceId = requestParameters != null ? requestParameters.get(PARAM_DEVICE_TOKEN) : null;
        if (clientInstanceId == null || clientInstanceId.length() == 0) {
            return superKey;
        }

        final Map<String, String> values = new LinkedHashMap<>(2);
        values.put(KEY_SUPER_KEY, superKey);
        values.put(KEY_CLIENT_INSTANCE_ID, clientInstanceId);

        return generateKey(values);
    }

}