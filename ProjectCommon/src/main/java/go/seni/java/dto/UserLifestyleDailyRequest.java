package go.seni.java.dto;

import lombok.Data;

import java.time.LocalDate;

@Data
public class UserLifestyleDailyRequest {
    private LocalDate date;
    private Integer steps;
    private Integer caloriesKcal;
    private Float weightKg;
    private Float waistCm;
    private Integer cigaretteCount;
}
