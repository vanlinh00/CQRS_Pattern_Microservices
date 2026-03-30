package go.seni.java.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;
import org.springframework.beans.factory.annotation.Value;

@Configuration
@Component
public class AppConfig {

    @Value("${security.oauth2.resource.tokenInfoUri}")
    public String tokenInfoUri;

    @Value("${security.oauth2.client.client-id}")
    public String clientId;

    @Value("${security.oauth2.client.client-secret}")
    public String clientSecret;
}
