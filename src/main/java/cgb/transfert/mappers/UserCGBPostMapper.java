package cgb.transfert.mappers;

import cgb.transfert.entities.Account;
import cgb.transfert.entities.Customer;
import cgb.transfert.entities.RoleCGB;
import cgb.transfert.entities.UserCGB;
import cgb.transfert.records.AccountPostRecord;
import cgb.transfert.records.UserCGBPostRecord;
import cgb.transfert.repositories.CustomerRepository;
import cgb.transfert.repositories.RoleCGBRepository;
import jakarta.persistence.EntityExistsException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class UserCGBPostMapper {

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private RoleCGBRepository roleCGBRepository;

    private final PasswordEncoder passwordEncoder;

    public UserCGBPostMapper(PasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
    }

    public UserCGB toEntity(UserCGBPostRecord userCGBPostRecord) {
        UserCGB userCGB = new UserCGB();
        userCGB.setUsername(userCGBPostRecord.username());
        userCGB.setPassword(passwordEncoder.encode(userCGBPostRecord.password()));
        Optional<Customer> customer = customerRepository.findById(userCGBPostRecord.customer_id());
        if (customer.isPresent()) {
            userCGB.setCustomer(customer.get());
        } else {
            throw new EntityExistsException("Customer not found");
        }
        Optional<RoleCGB> rolecgb = roleCGBRepository.findById(userCGBPostRecord.role_id());
        if (rolecgb.isPresent()) {
            userCGB.setRoleCGB(rolecgb.get());
        } else {
            throw new EntityExistsException("Role not found");
        }
        return userCGB;
    }
}
