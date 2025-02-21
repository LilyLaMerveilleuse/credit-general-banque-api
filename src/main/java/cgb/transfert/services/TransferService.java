package cgb.transfert.services;

import cgb.transfert.entities.Account;
import cgb.transfert.entities.Transfer;
import cgb.transfert.mappers.TransferPostMapper;
import cgb.transfert.records.TransferPostRecord;
import cgb.transfert.repositories.AccountRepository;
import cgb.transfert.repositories.TransferRepository;
import jakarta.persistence.EntityExistsException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
public class TransferService {

    private final TransferRepository transferRepository;
    private final TransferPostMapper transferPostMapper;
    private final AccountRepository accountRepository;


    @Autowired
    public TransferService(TransferRepository transferRepository, TransferPostMapper transferPostMapper,
                           AccountRepository accountRepository) {
        this.transferRepository = transferRepository;
        this.transferPostMapper = transferPostMapper;
        this.accountRepository = accountRepository;
    }

    public List<Transfer> getAllTransfers() {
        return transferRepository.findAll();
    }

    public Optional<Transfer> getTransferById(Long id) {
        return transferRepository.findById(id);
    }

    public Transfer saveTransfer(TransferPostRecord transferPost) {
        return transferRepository.save(transferPostMapper.toEntity(transferPost));
    }

    public void deleteTransfer(Long id) {
        transferRepository.deleteById(id);
    }

    public List<Transfer> getTransfersBySourceAccountNumber(String sourceAccountNumber) {
        Optional<Account> accountOptional = accountRepository.findById(sourceAccountNumber);
        if (accountOptional.isPresent()) {
            return transferRepository.findBySourceAccount(accountOptional.get());
        } else {
            throw new EntityExistsException("Le compte n°" + sourceAccountNumber + " n'as pas été trouvé.");
        }
    }

    public List<Transfer> getTransfersByDestinationAccountNumber(String destinationAccountNumber) {
        Optional<Account> accountOptional = accountRepository.findById(destinationAccountNumber);
        if (accountOptional.isPresent()) {
            return transferRepository.findByDestinationAccount(accountOptional.get());
        } else {
            throw new EntityExistsException("Le compte n°" + destinationAccountNumber + " n'as pas été trouvé.");
        }
    }
}
