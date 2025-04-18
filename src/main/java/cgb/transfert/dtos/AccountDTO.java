package cgb.transfert.dtos;

import cgb.transfert.annotations.ValidIban;
import cgb.transfert.entities.Account;
import cgb.transfert.repositories.CustomerRepository;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@Getter
public class AccountDTO {
    @ValidIban
    private String iban;
    private Double solde;
    private CustomerLightDTO customer;

    public AccountDTO(Account account) {
        this.iban = account.getIban();
        this.solde = account.getSolde();
        this.customer = account.getOwner() != null ? new CustomerLightDTO(account.getOwner()) : null;
    }
}
