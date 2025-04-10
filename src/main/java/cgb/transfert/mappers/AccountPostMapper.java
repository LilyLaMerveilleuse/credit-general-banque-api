package cgb.transfert.mappers;

import cgb.transfert.entities.Account;
import cgb.transfert.records.AccountPostRecord;
import org.springframework.stereotype.Component;

@Component
public class AccountPostMapper {
    public Account toEntity(AccountPostRecord accountPostRecord) {
        Account account = new Account();
        account.setIban(accountPostRecord.iban());
        account.setOwner_name(accountPostRecord.owner_name());
        account.setSolde(accountPostRecord.solde());
        return account;
    }
}
