package cgb.transfert.mappers;

import cgb.transfert.entities.Account;
import cgb.transfert.entities.Transfer;
import cgb.transfert.records.TransferPostRecord;
import cgb.transfert.repositories.AccountRepository;
import jakarta.persistence.EntityExistsException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class TransferPostMapper {

    @Autowired
    AccountRepository accountRepository;

    public Transfer toEntity(TransferPostRecord transferPostRecord) {
        Transfer transfer = new Transfer();
        transfer.setAmount(transferPostRecord.amount());
        transfer.setDescription(transferPostRecord.description());
        transfer.setTransferDate(transferPostRecord.transferDate());
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
