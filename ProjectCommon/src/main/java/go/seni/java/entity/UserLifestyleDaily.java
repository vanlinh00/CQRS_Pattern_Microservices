package go.seni.java.entity;

import lombok.*;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "user_lifestyle_daily")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserLifestyleDaily implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "user_id", nullable = false)
    private Long userId;

    @Column(name = "date", nullable = false)
    private LocalDate date;

    @Column(name = "steps")
    private Integer steps;

    @Column(name = "calories_kcal")
    private Integer caloriesKcal;

    @Column(name = "weight_kg")
    private Float weightKg;

    @Column(name = "waist_cm")
    private Float waistCm;

    @Column(name = "cigarette_count")
    private Integer cigaretteCount;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
}
