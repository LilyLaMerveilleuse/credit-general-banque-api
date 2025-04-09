package cgb.transfert.repositories;

import cgb.transfert.entities.TransferStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface TransferStatusRepository extends JpaRepository<TransferStatus, UUID> {
    public Optional<TransferStatus> findByName(String transferName);
}
