package cgb.transfert.controllers;

import cgb.transfert.dtos.CustomerDTO;
import cgb.transfert.entities.Customer;
import cgb.transfert.mappers.CustomerDTOMapper;
import cgb.transfert.services.CustomerService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/api/Customer")
public class CustomerController {

    private final CustomerService customerService;
    private final CustomerDTOMapper customerDTOMapper;

    @Autowired
    public CustomerController(CustomerService customerService, CustomerDTOMapper customerDTOMapper) {
        this.customerService = customerService;
        this.customerDTOMapper = customerDTOMapper;
    }

    @GetMapping
    public List<CustomerDTO> getAllCustomeres() {
        return customerDTOMapper.toDTO(
                customerService.getAllCustomers()
        );
    }

    @GetMapping("/{id}")
    public CustomerDTO getCustomerById(@PathVariable UUID id) {
        Optional<Customer> customer = customerService.getCustomerById(id);
        if (customer.isPresent()) {
            return customerDTOMapper.toDTO(
                    customer.get()
            );
        } else {
            throw new EntityNotFoundException("User not found");
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCustomer(@PathVariable UUID id) {
        customerService.deleteCustomer(id);
        return ResponseEntity.ok().build();
    }
}
