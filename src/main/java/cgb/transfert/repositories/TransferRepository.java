package cgb.transfert.repositories;

import cgb.transfert.entities.Transfer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TransferRepository extends JpaRepository<Transfer, Long> {
    List<Transfer> findBySourceAccountNumber(String sourceAccountNumber);
    List<Transfer> findByDestinationAccountNumber(String destinationAccountNumber);
}
