package cgb.transfert.dtos;

import cgb.transfert.entities.Customer;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Getter
public class CustomerLightDTO {
    private UUID id;
    private String name;
    private String adress;
    private String lei;

    public CustomerLightDTO(Customer customer) {
        this.id = customer.getId();
        this.name = customer.getName();
        this.adress = customer.getAddress();
        this.lei = customer.getLei();
    }
}
