package go.seni.java.services;

import go.seni.java.entity.User;
import go.seni.java.repository.UserRepository;
import go.seni.java.util.SecurityUtils;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

@Service
@Primary
public class CachedUserService implements UserService {

    private final UserRepository userRepository;

    public CachedUserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    @Cacheable(value = "users", key = "#id")
    public User getById(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found: id=" + id));
    }

    @Override
    public User getByLoginId(String loginId) {
        return userRepository.findByLoginId(loginId)
                .orElseThrow(() -> new RuntimeException("User not found: loginId=" + loginId));
    }

    @Override
    public User getCurrentUser() {
        Long id = SecurityUtils.currentUserId();
        return getById(id);
    }
}
