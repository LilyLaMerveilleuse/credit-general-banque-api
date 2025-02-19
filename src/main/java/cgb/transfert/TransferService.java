package cgb.transfert;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import jakarta.transaction.Transactional;
import java.time.LocalDate;

@Service
public class TransferService {

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private TransferRepository transferRepository;

    
    /*
     * Rappel du cours sur les transactions... Tout ou rien
     */
    @Transactional
    public Transfer createTransfer(String sourceAccountNumber, String destinationAccountNumber,
                                   Double amount, LocalDate transferDate, String description) {
        Account sourceAccount = accountRepository.findById(sourceAccountNumber)
                				.orElseThrow(() -> new RuntimeException("Source account not found"));
        Account destinationAccount = accountRepository.findById(destinationAccountNumber)
                				.orElseThrow(() -> new RuntimeException("Destination account not found"));

        /*Pas de découvert autorisé*/
        if (sourceAccount.getSolde().compareTo(amount) < 0) {
            throw new RuntimeException("Insufficient funds");
        }else {

        sourceAccount.setSolde(sourceAccount.getSolde()-(amount)); 
        destinationAccount.setSolde(destinationAccount.getSolde()+(amount));

        accountRepository.save(sourceAccount);
        accountRepository.save(destinationAccount);

        Transfer transfer = new Transfer();
        transfer.setSourceAccountNumber(sourceAccountNumber);
        transfer.setDestinationAccountNumber(destinationAccountNumber);
        transfer.setAmount(amount);
        transfer.setTransferDate(transferDate);
        transfer.setDescription(description);

        return transferRepository.save(transfer);
        }
    }
}
