package cgb.transfert.mappers;

import cgb.transfert.dtos.AccountDTO;
import cgb.transfert.entities.Account;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class AccountDTOMapper {

    public AccountDTO toDTO(Account account) {
        return new AccountDTO(account);
    }

    public List<AccountDTO> toDTO(List<Account> accounts) {
        List<AccountDTO> dtos = new ArrayList<>();

        for (Account account : accounts) {
            dtos.add(new AccountDTO(account));
        }

        return dtos;
    }
}
