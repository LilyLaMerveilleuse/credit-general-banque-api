package cgb.transfert.services;

import cgb.transfert.entities.Transfer;
import cgb.transfert.repositories.TransferRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
public class TransferService {

    private final TransferRepository transferRepository;

    @Autowired
    public TransferService(TransferRepository transferRepository) {
        this.transferRepository = transferRepository;
    }

    public List<Transfer> getAllTransfers() {
        return transferRepository.findAll();
    }

    public Optional<Transfer> getTransferById(Long id) {
        return transferRepository.findById(id);
    }

    public Transfer saveTransfer(Transfer transfer) {
        transfer.setTransferDate(LocalDate.now()); // Ajout automatique de la date
        return transferRepository.save(transfer);
    }

    public void deleteTransfer(Long id) {
        transferRepository.deleteById(id);
    }

    public List<Transfer> getTransfersBySourceAccount(String sourceAccount) {
        return transferRepository.findBySourceAccountNumber(sourceAccount);
    }

    public List<Transfer> getTransfersByDestinationAccount(String destinationAccount) {
        return transferRepository.findByDestinationAccountNumber(destinationAccount);
    }
}
