package go.seni.java.security;

import go.seni.java.config.AppConfig;
import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configurers.ResourceServerSecurityConfigurer;

import org.springframework.security.oauth2.provider.error.OAuth2AuthenticationEntryPoint;
import org.springframework.security.oauth2.provider.token.RemoteTokenServices;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;
import org.springframework.security.oauth2.provider.token.store.JwtTokenStore;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.charset.Charset;
import java.util.Arrays;

@Configuration
@EnableResourceServer
/*1) @Configuration + @EnableResourceServer
@Configuration: báo đây là class cấu hình Spring.
@EnableResourceServer: bật cơ chế OAuth2 Resource Server (kiểu cũ của Spring Security OAuth2).
Nghĩa là: mọi request vào API sẽ được Spring Security kiểm tra token theo chuẩn OAuth2.
Tại sao cần?

Vì bạn muốn bảo vệ các endpoint API bằng access token (JWT). Nếu không bật resource server thì app sẽ không tự xử lý Bearer token theo cơ chế OAuth2.
?
 */
public class ResourceServerConfig extends ResourceServerConfigurerAdapter {
    private static final Logger logger = LoggerFactory.getLogger(ResourceServerConfig.class);

    private final AppConfig appConfig;

    private String resourceId = "resource_id";


    private final CustomAccessTokenConverter customAccessTokenConverter;

    // private final CustomUserDetailsService userAccountService;

    private final SimpleCorsFilter filter;

    @Value("${security.origin.allow:false}")
    private boolean allowOrigin;

    @Autowired
    private CustomResponseExceptionTranslator customResponseExceptionTranslator;

    @Autowired
    public ResourceServerConfig(AppConfig appConfig, CustomAccessTokenConverter customAccessTokenConverter, /* CustomUserDetailsService userAccountService,*/ SimpleCorsFilter filter) {
        this.appConfig = appConfig;
        this.customAccessTokenConverter = customAccessTokenConverter;
        //    this.userAccountService = userAccountService;
        this.filter = filter;
    }

    @Bean
    @Primary
    public PasswordEncoder passwordEncoder() {
        return new CustomPasswordEncoder();
    }


    @Override
    public void configure(HttpSecurity http) throws Exception {

        /*allowOrigin = true (thường dev):
        cho phép frontend khác domain gọi API + cho phép chạy trong iframe (vd H2 console).*/
        if (allowOrigin) {
            http.cors().configurationSource(corsConfigurationSource()).and()
                    .csrf().disable()
                    .headers().frameOptions().disable();
        } else {
            http.cors()
                    .and()
                    .csrf().disable()
                    .headers().frameOptions().deny();
        }

        http.addFilterBefore(filter, BasicAuthenticationFilter.class)
                // .userDetailsService(userAccountService)
                .exceptionHandling()
                .authenticationEntryPoint
                        ((request, response, authException) -> response.sendError(HttpServletResponse.SC_UNAUTHORIZED))
                .and()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .authorizeRequests()
                .antMatchers(
                        "/api/api-docs/**",
                        "/api/swagger-ui/**",
                        "/api/users/**"
                ).permitAll()
                .and()
                .authorizeRequests()
                .anyRequest().authenticated();
    }

    /**
     * Decode Jwt Using PublicKey.
     *
     * @return JwtAccessTokenConverter
     */
    @Bean
    public JwtAccessTokenConverter accessTokenConverter() {
        final JwtAccessTokenConverter converter = new JwtAccessTokenConverter();
        converter.setAccessTokenConverter(customAccessTokenConverter);
        final Resource resource = new ClassPathResource("certificate/pubkey.txt");
        String publicKey;
        try {
            publicKey = IOUtils.toString(resource.getInputStream(), Charset.defaultCharset());
        } catch (final IOException e) {
            throw new UncheckedIOException(e);
        }
        converter.setVerifierKey(publicKey);
        return converter;
    }

    /**
     * We use remote token (Token from Authorization Server).
     * if token from Authorization Server deleted, resource server must revoke access token again.
     *
     * @return RemoteTokenServices.
     */
    @Primary
    @Bean
    public RemoteTokenServices tokenServices() {
        RemoteTokenServices tokenService = new RemoteTokenServices();
        tokenService.setCheckTokenEndpointUrl(
                this.appConfig.tokenInfoUri);
        tokenService.setAccessTokenConverter(customAccessTokenConverter);
        tokenService.setClientId(this.appConfig.clientId);
        tokenService.setClientSecret(this.appConfig.clientSecret);
        return tokenService;
    }


    /**
     * Matching resource_id on Authorization Server.
     *
     * @param resources ResourceServerSecurityConfigurer.
     * @throws Exception Error if token not accepted..
     */
    @Override
    public void configure(ResourceServerSecurityConfigurer resources) throws Exception {
        resources.resourceId(this.resourceId);
        resources.authenticationEntryPoint(new OAuth2AuthenticationEntryPoint() {{
            setExceptionTranslator(customResponseExceptionTranslator);
        }});
    }

    @Bean
    public TokenStore tokenStore() {
        return new JwtTokenStore(accessTokenConverter());
    }


    @Bean
    CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(Arrays.asList("*"));
        configuration.setAllowedMethods(Arrays.asList("GET", "HEAD", "POST", "PUT", "DELETE", "TRACE", "OPTIONS", "PATCH"));
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }

}
