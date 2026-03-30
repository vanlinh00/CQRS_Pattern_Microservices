package go.seni.java.security;

import go.seni.java.constants.ApiPath;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;

import javax.annotation.Resource;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    @Resource(name = "customUserDetailsService")
    private UserDetailsService userDetailsService;

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .csrf().disable()
                .authorizeRequests()
                // endpoint lấy token cho phép anonymous
                .antMatchers(ApiPath.OAUTH_TOKEN).permitAll()
                .antMatchers(ApiPath.OAUTH_LOGOUT).permitAll()
                .anyRequest().authenticated()
                .and()
                // TẮT basic auth ở đây để client Basic (mobile-client) không bị đưa vào UserDetailsService
                .httpBasic().disable()
                .formLogin().disable();
    }

    /*
    Vì httpBasic() chính là cơ chế mà Spring Security dùng để đọc header

    Authorization: Basic base64(username:password)

    rồi biến nó thành một lần authenticate bằng AuthenticationManager.

    Mà AuthenticationManager của bạn đang cấu hình:

    auth.userDetailsService(customUserDetailsService)
    => nghĩa là bất cứ khi nào có Basic Auth, Spring sẽ gọi:
customUserDetailsService.loadUserByUsername(usernameTrongBasic)

    */


    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
        auth
                .userDetailsService(this.userDetailsService)
                .passwordEncoder(new CustomPasswordEncoder());
    }

    @Override
    @Bean
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }
}