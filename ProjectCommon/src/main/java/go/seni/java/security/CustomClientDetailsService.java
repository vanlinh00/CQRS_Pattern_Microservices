package go.seni.java.security;

import go.seni.java.entity.OauthClientDetails;
import go.seni.java.repository.OauthClientDetailsRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.security.oauth2.provider.ClientDetails;
import org.springframework.security.oauth2.provider.ClientRegistrationException;
import org.springframework.security.oauth2.provider.client.BaseClientDetails;
import org.springframework.security.oauth2.provider.client.JdbcClientDetailsService;
import org.springframework.stereotype.Service;

import javax.sql.DataSource;
import java.util.Optional;


@Service
public class CustomClientDetailsService extends JdbcClientDetailsService {


    @Autowired
    private OauthClientDetailsRepository oauthClientDetailsRepository;

    public CustomClientDetailsService(DataSource dataSource) {
        super(dataSource);
    }

    @Override
    public ClientDetails loadClientByClientId(String clientId) throws ClientRegistrationException {
        Optional<OauthClientDetails> oauthClientDetails = oauthClientDetailsRepository.findByClientId(clientId);

        if (!oauthClientDetails.isPresent()) {
            throw new ClientRegistrationException("invalid_client");
        }

        OauthClientDetails client = oauthClientDetails.get();
        String resourceIds = String.join(",", client.getResourceIds());
        String scopes = String.join(",", client.getScope());
        String grantTypes = String.join(",", client.getAuthorizedGrantTypes());
        return new BaseClientDetails(client);
    }
}
