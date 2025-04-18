package cgb.transfert.services;

import cgb.transfert.entities.Account;
import cgb.transfert.entities.Transfer;
import cgb.transfert.entities.TransferStatus;
import cgb.transfert.enums.TransferStatusEnum;
import cgb.transfert.mappers.TransferPostMapper;
import cgb.transfert.records.TransferLotPostRecord;
import cgb.transfert.records.TransferLotUnitRecord;
import cgb.transfert.records.TransferPostRecord;
import cgb.transfert.repositories.AccountRepository;
import cgb.transfert.repositories.TransferRepository;
import jakarta.persistence.EntityExistsException;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class TransferService {

    private final TransferRepository transferRepository;
    private final TransferPostMapper transferPostMapper;
    private final AccountRepository accountRepository;
    private final TransferStatusService transferStatusService;
    private final CurrentUserService currentUserService;


    @Autowired
    public TransferService(TransferRepository transferRepository, TransferPostMapper transferPostMapper,
                           AccountRepository accountRepository, TransferStatusService transferStatusService, CurrentUserService currentUserService) {
        this.transferRepository = transferRepository;
        this.transferPostMapper = transferPostMapper;
        this.accountRepository = accountRepository;
        this.transferStatusService = transferStatusService;
        this.currentUserService = currentUserService;
    }

    public List<Transfer> getAllTransfers() {
        return transferRepository.findAll();
    }

    public Optional<Transfer> getTransferById(UUID id) {
        return transferRepository.findById(id);
    }

    public Transfer saveTransfer(Transfer transfer) {
        if (!currentUserService.getCurrentUser().getCustomer().getAccounts().contains(transfer.getSourceAccount())) {
            throw new RuntimeException("Cet utilisateur ne possède pas le compte source");
        }
        Account sourceAccount = transfer.getSourceAccount();
        Account destinationAccount = transfer.getDestinationAccount();
        if (sourceAccount.getSolde() >= transfer.getAmount()) {
            if (sourceAccount.getOwner().getBeneficiaryAccounts().contains(destinationAccount)) {
                sourceAccount.setSolde(sourceAccount.getSolde() - transfer.getAmount());
                destinationAccount.setSolde(destinationAccount.getSolde() + transfer.getAmount());
                Optional<TransferStatus> status = transferStatusService.getTransferStatusByName(TransferStatusEnum.DONE.getLabel());
                transfer.setStatus(status.orElseThrow());
            } else {
                Optional<TransferStatus> status = transferStatusService.getTransferStatusByName(TransferStatusEnum.UNAUTHORIZED.getLabel());
                transfer.setStatus(status.orElseThrow());
            }
        } else {
            Optional<TransferStatus> status = transferStatusService.getTransferStatusByName(TransferStatusEnum.ERROR.getLabel());
            transfer.setStatus(status.orElseThrow());
        }
        return transferRepository.save(transfer);
    }

    public List<TransferPostRecord> formatTransfers(TransferLotPostRecord transfers) {
        List<TransferPostRecord> transferPostRecords = new ArrayList<>();
        for (TransferLotUnitRecord transfer : transfers.transfers()){
            transferPostRecords.add(
                    new TransferPostRecord(
                            transfer.amount(),
                            transfer.description(),
                            transfers.iban(),
                            transfer.iban()
                    )
            );
        }
        return transferPostRecords;
    }

    public List<Transfer> saveTransfers(List<Transfer> transfers) {
        UUID uuid = UUID.randomUUID();
        List<Transfer> savedTransfers = new ArrayList<>();
        for (Transfer transfer : transfers) {
            transfer.setLotId(uuid);
            savedTransfers.add(saveTransfer(transfer));
        }
        return savedTransfers;
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

    public List<Transfer> getTransfersByLotId(UUID lotId) {
        return transferRepository.findByLotId(lotId);
    }
}
