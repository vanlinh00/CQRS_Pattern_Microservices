package go.seni.java.dto;

import go.seni.java.entity.UserLifestyleDaily;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@Builder
public class UserLifestyleDailyResponse {
    private Long id;
    private Long userId;
    private LocalDate date;
    private Integer steps;
    private Integer caloriesKcal;
    private Float weightKg;
    private Float waistCm;
    private Integer cigaretteCount;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public static UserLifestyleDailyResponse from(UserLifestyleDaily e) {
        return UserLifestyleDailyResponse.builder()
                .id(e.getId())
                .userId(e.getUserId())
                .date(e.getDate())
                .steps(e.getSteps())
                .caloriesKcal(e.getCaloriesKcal())
                .weightKg(e.getWeightKg())
                .waistCm(e.getWaistCm())
                .cigaretteCount(e.getCigaretteCount())
                .createdAt(e.getCreatedAt())
                .updatedAt(e.getUpdatedAt())
                .build();
    }
}
