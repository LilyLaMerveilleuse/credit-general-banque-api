package cgb.transfert.dtos;

import cgb.transfert.annotations.ValidIban;
import cgb.transfert.entities.Account;
import lombok.Getter;

import java.util.Set;
import java.util.stream.Collectors;

@Getter
public class AccountDTO {
    @ValidIban
    private String iban;
    private String ownerName;
    private Double solde;
    private Set<String> beneficiaireIbans;

    public AccountDTO(Account account) {
        this.iban = account.getIban();
        this.ownerName = account.getOwner_name();
        this.solde = account.getSolde();
        this.beneficiaireIbans = account.getBeneficiaires()
                .stream()
                .map(Account::getIban)
                .collect(Collectors.toSet());
    }
}
