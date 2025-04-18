package cgb.transfert.dtos;

import cgb.transfert.annotations.ValidIban;
import cgb.transfert.entities.Customer;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Getter
public class CustomerDTO {
    private UUID id;
    private String name;
    private String adress;
    private String lei;
    private List<AccountLightDTO> accounts;
    private List<AccountLightDTO> beneficiaries;

    public CustomerDTO(Customer customer) {
        this.id = customer.getId();
        this.name = customer.getName();
        this.adress = customer.getAddress();
        this.lei = customer.getLei();
        this.accounts = customer.getAccounts().stream().map(AccountLightDTO::new).collect(Collectors.toList());
        this.beneficiaries = customer.getBeneficiaryAccounts().stream().map(AccountLightDTO::new).collect(Collectors.toList());
    }
}
