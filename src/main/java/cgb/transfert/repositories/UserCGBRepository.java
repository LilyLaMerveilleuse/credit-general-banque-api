package cgb.transfert.repositories;

import cgb.transfert.entities.Account;
import cgb.transfert.entities.UserCGB;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface UserCGBRepository extends JpaRepository<UserCGB, UUID> {
    Optional<UserCGB> findByUsername(String username);
}
