package cgb.transfert.repositories;

import cgb.transfert.controllers.AccountController;
import cgb.transfert.entities.Account;
import cgb.transfert.entities.Transfer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface TransferRepository extends JpaRepository<Transfer, UUID> {
    List<Transfer> findBySourceAccount(Account sourceAccount);
    List<Transfer> findByDestinationAccount(Account destinationAccount);
    List<Transfer> findByLotId(UUID lotId);
}
