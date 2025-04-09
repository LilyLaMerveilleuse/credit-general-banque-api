package cgb.transfert.mappers;

import cgb.transfert.entities.Account;
import cgb.transfert.records.AccountPostRecord;
import cgb.transfert.repositories.AccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Random;

@Component
public class AccountPostMapper {

    @Autowired
    AccountRepository accountRepository;

    private final Random random = new Random();

    public Account toEntity(AccountPostRecord accountPostRecord) {
        Account account = new Account();
        account.setIban(accountPostRecord.iban());
        account.setOwner_name(accountPostRecord.owner_name());
        account.setSolde(accountPostRecord.solde());
        return account;
    }
}
