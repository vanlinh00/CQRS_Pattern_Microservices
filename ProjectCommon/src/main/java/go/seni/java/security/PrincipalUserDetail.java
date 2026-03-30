//package go.seni.java.security;
//
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.security.core.GrantedAuthority;
//import org.springframework.security.core.authority.SimpleGrantedAuthority;
//import org.springframework.security.core.userdetails.UserDetails;
//
//import go.seni.java.entity.User;
//
//import java.util.Collection;
//import java.util.Collections;
//import java.util.Objects;
//
///**
// * Used By UserDetailsService.
// * This class only uses User entity (Admin references removed).
// */
//public class PrincipalUserDetail implements UserDetails {
//    private static final Logger LOG = LoggerFactory.getLogger(PrincipalUserDetail.class);
//
//    private final User user;
//
//
//
//    public PrincipalUserDetail(User user) {
//        this.user = user;
//
//    }
//
//    @Override
//    public Collection<? extends GrantedAuthority> getAuthorities() {
//        // Example: use role from User, otherwise default ROLE_USER
////        if (Objects.nonNull(this.user) && this.user.getRole() != null) {
////            return Collections.singleton(new SimpleGrantedAuthority(this.user.getRole().name()));
////        }
//        return Collections.singleton(new SimpleGrantedAuthority("ROLE_USER"));
//    }
//
//    @Override
//    public String getPassword() {
//        return this.user.getPasswordHash();
//    }
//
//    @Override
//    public String getUsername() {
//        return this.user.getLoginId();
//    }
//
//    public String getName() {
//        return this.user.getLoginId();
//    }
//
//    public User getUser() {
//        return this.user;
//    }
//
//    @Override
//    public boolean isAccountNonExpired() {
//        return true; // customize logic if needed
//    }
//
//    @Override
//    public boolean isAccountNonLocked() {
//        return true; // customize logic if needed
//    }
//
//    @Override
//    public boolean isCredentialsNonExpired() {
//        return true; // customize logic if needed
//    }
//
//    @Override
//    public boolean isEnabled() {
//        return true; // customize logic if needed
//    }
//}