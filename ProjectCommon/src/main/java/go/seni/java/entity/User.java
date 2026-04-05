    package go.seni.java.entity;

    import lombok.*;

    import javax.persistence.*;
    import java.io.Serializable;
    import java.time.LocalDate;
    import java.time.LocalDateTime;
    import java.util.Collection;
    import java.util.Collections;

    import org.springframework.security.core.GrantedAuthority;
    import org.springframework.security.core.authority.SimpleGrantedAuthority;
    import org.springframework.security.core.userdetails.UserDetails;

    @Entity
    @Table(name = "users")
    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public class User implements Serializable {

        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        private Long id;

        @Column(name = "group_id")
        private Long groupId;

        @Column(name = "login_id", nullable = false, unique = true, length = 20)
        private String loginId;

        @Column(name = "password_hash", nullable = false, length = 128)
        private String passwordHash;

        @Column(name = "full_name", length = 128)
        private String fullName;

        @Column(name = "gender", nullable = false, length = 1, columnDefinition = "varchar(1) default 'U'")
        private String gender = "U";

        @Column(name = "date_of_birth")
        private LocalDate dateOfBirth;

        @Column(name = "height_cm")
        private Float heightCm;

        @Column(name = "email", nullable = false, length = 255)
        private String email;

        @Column(name = "weight_kg")
        private Float weightKg;

        @Column(name = "point")
        private Integer point;

        @Column(name = "created_at", nullable = false, updatable = false,
                columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
        private LocalDateTime createdAt;

        @Column(name = "updated_at")
        private LocalDateTime updatedAt;

        @Column(name = "creator", nullable = false, length = 255, columnDefinition = "varchar(255) default 'system'")
        private String creator = "system";

        @Column(name = "updater", length = 255)
        private String updater;

        @Column(name = "role", nullable = false, length = 50)
        private String role = "ROLE_USER";

        @Column(name = "is_deleted", nullable = false)
        private Boolean isDeleted = false;


    }