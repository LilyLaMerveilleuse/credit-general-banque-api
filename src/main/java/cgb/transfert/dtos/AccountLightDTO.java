package cgb.transfert.dtos;

import cgb.transfert.annotations.ValidIban;
import cgb.transfert.entities.Account;
import lombok.Getter;

@Getter
public class AccountLightDTO {
    @ValidIban
    private String iban;
    private Double solde;

    public AccountLightDTO(Account account) {
        this.iban = account.getIban();
        this.solde = account.getSolde();
    }
}
