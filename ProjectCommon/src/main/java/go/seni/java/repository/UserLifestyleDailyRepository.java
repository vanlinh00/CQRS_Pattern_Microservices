package go.seni.java.repository;

import go.seni.java.entity.UserLifestyleDaily;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface UserLifestyleDailyRepository extends JpaRepository<UserLifestyleDaily, Long> {

    List<UserLifestyleDaily> findByUserIdOrderByDateDesc(Long userId);

    Optional<UserLifestyleDaily> findByUserIdAndDate(Long userId, LocalDate date);

    boolean existsByUserIdAndDate(Long userId, LocalDate date);
}
