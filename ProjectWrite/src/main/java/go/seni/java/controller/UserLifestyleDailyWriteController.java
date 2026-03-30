package go.seni.java.controller;

import go.seni.java.Factory.ResponseFactory;
import go.seni.java.dto.UserLifestyleDailyRequest;
import go.seni.java.dto.UserLifestyleDailyResponse;
import go.seni.java.entity.UserLifestyleDaily;
import go.seni.java.enums.MessageCode;
import go.seni.java.repository.UserLifestyleDailyRepository;
import go.seni.java.util.SecurityUtils;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.time.LocalDate;
import java.time.LocalDateTime;

@RestController
@RequestMapping("api/lifestyle")
public class UserLifestyleDailyWriteController {

    private final UserLifestyleDailyRepository repository;

    public UserLifestyleDailyWriteController(UserLifestyleDailyRepository repository) {
        this.repository = repository;
    }

    @PreAuthorize("hasRole('ROLE_USER')")
    @PostMapping
    public ResponseEntity<?> create(@Valid @RequestBody UserLifestyleDailyRequest request) {
        Long userId = SecurityUtils.currentUserId();
        LocalDate date = request.getDate() != null ? request.getDate() : LocalDate.now();

        if (repository.existsByUserIdAndDate(userId, date)) {
            return ResponseFactory.error(MessageCode.INVALID_ARGUMENT.getCode(), "Record already exists for this date.");
        }

        UserLifestyleDaily entity = mapToEntity(userId, request);
        entity.setCreatedAt(LocalDateTime.now());
        UserLifestyleDaily saved = repository.save(entity);

        return ResponseFactory.success(UserLifestyleDailyResponse.from(saved));
    }

    @PreAuthorize("hasRole('ROLE_USER')")
    @PutMapping("/{id}")
    public ResponseEntity<?> update(@PathVariable Long id, @Valid @RequestBody UserLifestyleDailyRequest request) {
        Long userId = SecurityUtils.currentUserId();
        UserLifestyleDaily existing = repository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Not found: id=" + id));

        if (!existing.getUserId().equals(userId)) {
            return ResponseFactory.error(MessageCode.FORBIDDEN.getCode(), "Access denied.");
        }

        existing.setDate(request.getDate() != null ? request.getDate() : existing.getDate());
        existing.setSteps(request.getSteps());
        existing.setCaloriesKcal(request.getCaloriesKcal());
        existing.setWeightKg(request.getWeightKg());
        existing.setWaistCm(request.getWaistCm());
        existing.setCigaretteCount(request.getCigaretteCount());
        existing.setUpdatedAt(LocalDateTime.now());
        UserLifestyleDaily saved = repository.save(existing);

        return ResponseFactory.success(UserLifestyleDailyResponse.from(saved));
    }

    @PreAuthorize("hasRole('ROLE_USER')")
    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable Long id) {
        Long userId = SecurityUtils.currentUserId();
        UserLifestyleDaily existing = repository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Not found: id=" + id));

        if (!existing.getUserId().equals(userId)) {
            return ResponseFactory.error(MessageCode.FORBIDDEN.getCode(), "Access denied.");
        }

        repository.delete(existing);
        return ResponseFactory.success();
    }

    private UserLifestyleDaily mapToEntity(Long userId, UserLifestyleDailyRequest req) {
        UserLifestyleDaily e = new UserLifestyleDaily();
        e.setUserId(userId);
        e.setDate(req.getDate() != null ? req.getDate() : LocalDate.now());
        e.setSteps(req.getSteps());
        e.setCaloriesKcal(req.getCaloriesKcal());
        e.setWeightKg(req.getWeightKg());
        e.setWaistCm(req.getWaistCm());
        e.setCigaretteCount(req.getCigaretteCount());
        return e;
    }
}
