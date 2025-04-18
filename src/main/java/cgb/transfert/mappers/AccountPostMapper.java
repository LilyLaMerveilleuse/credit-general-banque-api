package cgb.transfert.mappers;

import cgb.transfert.entities.Account;
import cgb.transfert.entities.Customer;
import cgb.transfert.records.AccountPostRecord;
import cgb.transfert.repositories.CustomerRepository;
import jakarta.persistence.EntityExistsException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class AccountPostMapper {

    @Autowired
    private CustomerRepository customerRepository;

    public Account toEntity(AccountPostRecord accountPostRecord) {
        Account account = new Account();
        account.setIban(accountPostRecord.iban());
        account.setSolde(accountPostRecord.solde());
        Optional<Customer> customer = customerRepository.findById(accountPostRecord.customerId());
        if (customer.isPresent()) {
            account.setOwner(customer.get());
        } else {
            throw new EntityExistsException("Customer not found");
        }
        return account;
    }
}
