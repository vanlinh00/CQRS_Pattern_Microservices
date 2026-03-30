package go.seni.java.repository;

import go.seni.java.entity.OauthClientDetails;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;


@Repository
public interface OauthClientDetailsRepository extends JpaRepository<OauthClientDetails, Long> {

    Optional<OauthClientDetails> findByClientId(String clientId);

}
