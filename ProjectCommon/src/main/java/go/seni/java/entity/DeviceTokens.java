package go.seni.java.entity;

import lombok.*;
import javax.persistence.*;
import java.time.Instant;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DeviceTokens {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "user_id")
    private Long userId;

    @Column(name = "jti", nullable = false, length = 100)
    private String jti;

    @Column(name = "device_token", nullable = false, unique = true, length = 1000)
    private String deviceToken;

    @Column(name = "creator", length = 255)
    private String creator;

    @Column(name = "created_at")
    private Instant createdAt;

    @Column(name = "updater", length = 255)
    private String updater;

    @Column(name = "updated_at")
    private Instant updatedAt;
}