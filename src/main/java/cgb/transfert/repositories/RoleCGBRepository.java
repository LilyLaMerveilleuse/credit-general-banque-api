package cgb.transfert.repositories;

import cgb.transfert.entities.RoleCGB;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface RoleCGBRepository extends JpaRepository<RoleCGB, UUID> {
}
