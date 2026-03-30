//// Repo: DeviceTokenRepository.java
//package go.seni.java.repository;
//
//
//import org.springframework.data.jpa.repository.JpaRepository;
//import org.springframework.stereotype.Repository;
//
//import java.util.List;
//import java.util.Optional;
//
//@Repository
//public interface DeviceTokenRepository extends JpaRepository<DeviceToken, Long> {
//
//    Optional<DeviceToken> findByDeviceToken(String deviceToken);
//
//    Optional<DeviceToken> findByJti(String jti);
//
//    List<DeviceToken> findAllByUserId(Long userId);
//
//    long deleteByDeviceToken(String deviceToken);
//
//    long deleteByJti(String jti);
//
//    long deleteAllByUserId(Long userId);
//}