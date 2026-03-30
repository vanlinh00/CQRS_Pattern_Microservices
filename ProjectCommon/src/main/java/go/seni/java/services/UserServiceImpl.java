package go.seni.java.services;

import go.seni.java.entity.User;
import go.seni.java.repository.UserRepository;
import go.seni.java.util.SecurityUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    @Override
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
        return userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found id=" + id));

    }

//    @Transactional
//    @Override
//    public User updateProfile(String loginId, UpdateProfileRequest req) {
//        User u = getByLoginId(loginId);
//
//        if (req.getFullName() != null) u.setFullName(req.getFullName());
//        if (req.getGender() != null) u.setGender(req.getGender());
//        if (req.getDateOfBirth() != null) u.setDateOfBirth(req.getDateOfBirth());
//        if (req.getHeightCm() != null) u.setHeightCm(req.getHeightCm());
//        if (req.getWeightKg() != null) u.setWeightKg(req.getWeightKg());
//        if (req.getEmail() != null) u.setEmail(req.getEmail());
//
//        u.setUpdatedAt(LocalDateTime.now());
//        // u.setUpdater(loginId); // nếu muốn set updater theo user hiện tại
//
//        return userRepository.save(u);
//    }
}