package go.seni.java.controller;

import go.seni.java.Factory.ResponseFactory;
import go.seni.java.dto.UserLifestyleDailyResponse;
import go.seni.java.enums.MessageCode;
import go.seni.java.entity.UserLifestyleDaily;
import go.seni.java.repository.UserLifestyleDailyRepository;
import go.seni.java.util.SecurityUtils;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("api/lifestyle")
public class UserLifestyleDailyReadController {

    private final UserLifestyleDailyRepository repository;

    public UserLifestyleDailyReadController(UserLifestyleDailyRepository repository) {
        this.repository = repository;
    }

    @PreAuthorize("hasRole('ROLE_USER')")
    @GetMapping
    public ResponseEntity<?> list() {
        Long userId = SecurityUtils.currentUserId();
        List<UserLifestyleDaily> list = repository.findByUserIdOrderByDateDesc(userId);
        List<UserLifestyleDailyResponse> responses = list.stream()
                .map(UserLifestyleDailyResponse::from)
                .collect(Collectors.toList());
        return ResponseFactory.success(responses);
    }

    @PreAuthorize("hasRole('ROLE_USER')")
    @GetMapping("/{id}")
    public ResponseEntity<?> getById(@PathVariable Long id) {
        Long userId = SecurityUtils.currentUserId();
        UserLifestyleDaily entity = repository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Not found: id=" + id));

        if (!entity.getUserId().equals(userId)) {
            return ResponseFactory.error(MessageCode.FORBIDDEN.getCode(), "Access denied.");
        }

        return ResponseFactory.success(UserLifestyleDailyResponse.from(entity));
    }

    @PreAuthorize("hasRole('ROLE_USER')")
    @GetMapping("/date/{date}")
    public ResponseEntity<?> getByDate(@PathVariable LocalDate date) {
        Long userId = SecurityUtils.currentUserId();
        return repository.findByUserIdAndDate(userId, date)
                .map(e -> ResponseFactory.success(UserLifestyleDailyResponse.from(e)))
                .orElse(ResponseFactory.error(MessageCode.NOT_FOUND.getCode(), "Not found for date: " + date));
    }
}
