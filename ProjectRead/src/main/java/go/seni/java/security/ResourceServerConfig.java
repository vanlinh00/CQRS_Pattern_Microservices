package go.seni.java.security;

import go.seni.java.config.AppConfig;
import org.apache.commons.io.IOUtils;
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
public class ResourceServerConfig extends ResourceServerConfigurerAdapter {

    private final AppConfig appConfig;
    private final CustomAccessTokenConverter customAccessTokenConverter;
    private final SimpleCorsFilter filter;

    @Value("${security.origin.allow:false}")
    private boolean allowOrigin;

    @Autowired
    private CustomResponseExceptionTranslator customResponseExceptionTranslator;

    @Autowired
    public ResourceServerConfig(AppConfig appConfig, CustomAccessTokenConverter customAccessTokenConverter, SimpleCorsFilter filter) {
        this.appConfig = appConfig;
        this.customAccessTokenConverter = customAccessTokenConverter;
        this.filter = filter;
    }

    @Bean
    @Primary
    public PasswordEncoder passwordEncoder() {
        return new CustomPasswordEncoder();
    }

    @Override
    public void configure(HttpSecurity http) throws Exception {
        if (allowOrigin) {
            http.cors().configurationSource(corsConfigurationSource()).and()
                    .csrf().disable()
                    .headers().frameOptions().disable();
        } else {
            http.cors().and()
                    .csrf().disable()
                    .headers().frameOptions().deny();
        }

        http.addFilterBefore(filter, BasicAuthenticationFilter.class)
                .exceptionHandling()
                .authenticationEntryPoint((request, response, authException) -> response.sendError(HttpServletResponse.SC_UNAUTHORIZED))
                .and()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .authorizeRequests()
                .antMatchers("/api/users/**").permitAll()
                .and()
                .authorizeRequests()
                .anyRequest().authenticated();
    }

    @Bean
    public JwtAccessTokenConverter accessTokenConverter() {
        final JwtAccessTokenConverter converter = new JwtAccessTokenConverter();
        converter.setAccessTokenConverter(customAccessTokenConverter);
        final Resource resource = new ClassPathResource("certificate/pubkey.txt");
        try {
            String publicKey = IOUtils.toString(resource.getInputStream(), Charset.defaultCharset());
            converter.setVerifierKey(publicKey);
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
        return converter;
    }

    @Primary
    @Bean
    public RemoteTokenServices tokenServices() {
        RemoteTokenServices tokenService = new RemoteTokenServices();
        tokenService.setCheckTokenEndpointUrl(this.appConfig.tokenInfoUri);
        tokenService.setAccessTokenConverter(customAccessTokenConverter);
        tokenService.setClientId(this.appConfig.clientId);
        tokenService.setClientSecret(this.appConfig.clientSecret);
        return tokenService;
    }

    @Override
    public void configure(ResourceServerSecurityConfigurer resources) {
        resources.resourceId("resource_id");
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
