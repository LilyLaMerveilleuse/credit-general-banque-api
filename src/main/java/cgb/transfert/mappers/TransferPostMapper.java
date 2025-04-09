package cgb.transfert.mappers;

import cgb.transfert.enums.TransferStatusEnum;
import cgb.transfert.entities.Account;
import cgb.transfert.entities.Transfer;
import cgb.transfert.entities.TransferStatus;
import cgb.transfert.records.TransferPostRecord;
import cgb.transfert.repositories.AccountRepository;
import cgb.transfert.repositories.TransferStatusRepository;
import jakarta.persistence.EntityExistsException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.Optional;

@Component
public class TransferPostMapper {

    @Autowired
    AccountRepository accountRepository;
    @Autowired
    private TransferStatusRepository transferStatusRepository;

    public Transfer toEntity(TransferPostRecord transferPostRecord) {
        Transfer transfer = new Transfer();
        transfer.setAmount(transferPostRecord.amount());
        transfer.setTransfer_date(LocalDate.now());
        transfer.setDescription(transferPostRecord.description());
        Optional<TransferStatus> transferStatus = transferStatusRepository.findByName(TransferStatusEnum.NEW.getLabel());
        if (transferStatus.isPresent()) {
            transfer.setStatus(transferStatus.get());
        } else {
            throw new EntityExistsException("Status de tranfer non trouvé");
        }
        Optional<Account> sourceAccount = accountRepository.findById(transferPostRecord.sourceAccountNumber());
        if (sourceAccount.isPresent()) {
            transfer.setSourceAccount(sourceAccount.get());
        } else {
            throw new EntityExistsException("Compte non trouvé avec ce numéro");
        }
        Optional<Account> destinationAccount = accountRepository.findById(transferPostRecord.destinationAccountNumber());
        if (destinationAccount.isPresent()) {
            transfer.setDestinationAccount(destinationAccount.get());
        } else {
            throw new EntityExistsException("Compte non trouvé avec ce numéro");
        }
        return transfer;
    }
}
