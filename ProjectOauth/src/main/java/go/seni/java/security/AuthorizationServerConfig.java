package go.seni.java.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.common.exceptions.OAuth2Exception;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.ClientDetailsService;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.JdbcTokenStore;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;
import org.springframework.security.oauth2.provider.token.store.KeyStoreKeyFactory;

import javax.annotation.Resource;
import javax.sql.DataSource;
import java.security.KeyPair;

@Configuration
@EnableAuthorizationServer
public class AuthorizationServerConfig extends AuthorizationServerConfigurerAdapter {

    private final DataSource dataSource;
    private final AuthenticationManager authenticationManager;

    private final static String PASS_KEY = "123456";


    @Resource(name = "customUserDetailsService")
    private UserDetailsService userDetailsService;

    private final CustomAccessTokenConverter customAccessTokenConverter;
    private final CustomResponseExceptionTranslator exceptionTranslator;

    @Autowired
    public AuthorizationServerConfig(DataSource dataSource,
                                     AuthenticationManager authenticationManager, CustomAccessTokenConverter customAccessTokenConverter, CustomResponseExceptionTranslator exceptionTranslator) {
        this.dataSource = dataSource;
        this.authenticationManager = authenticationManager;
        this.customAccessTokenConverter = customAccessTokenConverter;
        this.exceptionTranslator = exceptionTranslator;
    }

    @Bean
    public ClientDetailsService clientDetailsService() {
        CustomClientDetailsService client = new CustomClientDetailsService(this.dataSource);
        client.setPasswordEncoder(this.passwordEncoder());
        return client;
    }

    @Bean
    public TokenStore tokenStore() {
        JdbcTokenStore tokenStore = new CustomJdbcTokenStore(this.dataSource);
        tokenStore.setAuthenticationKeyGenerator(new EnhancedAuthenticationKeyGenerator());
        return tokenStore;
    }

    @Bean
    public JwtAccessTokenConverter accessTokenConverter() {

        JwtAccessTokenConverter converter = new JwtAccessTokenConverter();
        KeyPair keyPair = new KeyStoreKeyFactory(
                new ClassPathResource("certificate/jwt.p12"),
                PASS_KEY.toCharArray())
                .getKeyPair("jwt");
        //Setup keypair
        converter.setKeyPair(keyPair);
        converter.setAccessTokenConverter(customAccessTokenConverter);
        return converter;
    }

    @Override
    public void configure(ClientDetailsServiceConfigurer clients) throws Exception {
        clients.withClientDetails(this.clientDetailsService());
    }

    @Override
    public void configure(AuthorizationServerSecurityConfigurer security) {
        // Cho phép client gửi client_id/client_secret bằng form (optional)
        // Nếu bạn dùng Basic Auth thì vẫn OK khi bật dòng này
       // security.allowFormAuthenticationForClients();

        //Gắn PasswordEncoder để server dùng khi xác thực client (client_id/client_secret) hoặc xử lý các mật khẩu/secret liên quan.
        security.passwordEncoder(this.passwordEncoder())
                .tokenKeyAccess("permitAll()") //  Cấu hình quyền truy cập endpoint /oauth/token_key (nơi public key dùng để verify JWT, nếu bạn dùng JWT).
                .checkTokenAccess("isAuthenticated()")
        //"isAuthenticated()" = chỉ cho phép khi request đã được xác thực
        // (thường là client đã đăng nhập bằng Basic Auth với client_id/client_secret).


            /*
            Ép luồng xử lý lỗi: Khi bất kỳ lỗi xác thực nào xảy ra ở tầng Filter
                (kể cả trong CustomUserDetailsService),
             thay vì trả về 401 mặc định của Spring, hệ thống sẽ gọi đến exceptionTranslator của bạn.
             */
        .authenticationEntryPoint((request, response, authException) -> {
            ResponseEntity<OAuth2Exception> out = null;
            try {
                out = exceptionTranslator.translate(authException);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
            response.setStatus(out.getStatusCodeValue());
            response.setContentType("application/json;charset=UTF-8");
            response.getWriter().write(new ObjectMapper().writeValueAsString(out.getBody()));
        });
    }

    @Override
    public void configure(AuthorizationServerEndpointsConfigurer endpoints) {
        endpoints
                // dùng để authenticate USER cho grant_type=password
                .authenticationManager(authenticationManager)
                .exceptionTranslator(exceptionTranslator)

                .tokenStore(this.tokenStore())
                // rất quan trọng để load USER (someonddd) và support refresh_token đúng
                .userDetailsService(userDetailsService)
                .accessTokenConverter(accessTokenConverter()); // rất quan trọng

    }

    @Bean
    @Primary
    public PasswordEncoder passwordEncoder() {
        return new CustomPasswordEncoder();
    }
}