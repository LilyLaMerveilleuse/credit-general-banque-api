package cgb.transfert.mappers;

import cgb.transfert.dtos.CustomerDTO;
import cgb.transfert.entities.Customer;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class CustomerDTOMapper {

    public CustomerDTO toDTO(Customer customer) {
        return new CustomerDTO(customer);
    }

    public List<CustomerDTO> toDTO(List<Customer> customers) {
        List<CustomerDTO> dtos = new ArrayList<>();

        for (Customer customer : customers) {
            dtos.add(new CustomerDTO(customer));
        }

        return dtos;
    }
}
