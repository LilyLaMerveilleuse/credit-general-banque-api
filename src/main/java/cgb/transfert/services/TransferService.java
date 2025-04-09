package cgb.transfert.services;

import cgb.transfert.entities.Account;
import cgb.transfert.entities.Transfer;
import cgb.transfert.entities.TransferStatus;
import cgb.transfert.enums.TransferStatusEnum;
import cgb.transfert.mappers.TransferPostMapper;
import cgb.transfert.records.TransferPostRecord;
import cgb.transfert.repositories.AccountRepository;
import cgb.transfert.repositories.TransferRepository;
import jakarta.persistence.EntityExistsException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class TransferService {

    private final TransferRepository transferRepository;
    private final TransferPostMapper transferPostMapper;
    private final AccountRepository accountRepository;
    private final TransferStatusService transferStatusService;


    @Autowired
    public TransferService(TransferRepository transferRepository, TransferPostMapper transferPostMapper,
                           AccountRepository accountRepository, TransferStatusService transferStatusService) {
        this.transferRepository = transferRepository;
        this.transferPostMapper = transferPostMapper;
        this.accountRepository = accountRepository;
        this.transferStatusService = transferStatusService;
    }

    public List<Transfer> getAllTransfers() {
        return transferRepository.findAll();
    }

    public Optional<Transfer> getTransferById(UUID id) {
        return transferRepository.findById(id);
    }

    public Transfer saveTransfer(TransferPostRecord transferPost) {
        Transfer transfer = transferPostMapper.toEntity(transferPost);
        Account sourceAccount = transfer.getSourceAccount();
        Account destinationAccount = transfer.getDestinationAccount();
        if (sourceAccount.getSolde() >= transfer.getAmount()) {
            sourceAccount.setSolde(sourceAccount.getSolde() - transfer.getAmount());
            destinationAccount.setSolde(destinationAccount.getSolde() + transfer.getAmount());
            Optional<TransferStatus> status = transferStatusService.getTransferStatusByName(TransferStatusEnum.DONE.getLabel());
            transfer.setStatus(status.orElseThrow());
        } else {
            Optional<TransferStatus> status = transferStatusService.getTransferStatusByName(TransferStatusEnum.CANCELLED.getLabel());
            transfer.setStatus(status.orElseThrow());
        }
        return transferRepository.save(transfer);
    }

    public void deleteTransfer(UUID id) {
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
