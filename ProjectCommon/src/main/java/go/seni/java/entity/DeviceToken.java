//package go.seni.java.entity;
//
//import javax.persistence.*;
//import java.time.LocalDateTime;
//
//@Entity
//@Table(name = "device_tokens")
//public class DeviceToken {
//
//    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    private Long id;
//
//    private Long userId;
//
//    @Column(nullable = false, length = 100)
//    private String jti;
//
//    @Column(nullable = false, unique = true, length = 1000)
//    private String deviceToken;
//
//    private String creator;
//
//    private LocalDateTime createdAt;
//
//    private String updater;
//
//    private LocalDateTime updatedAt;
//
//}