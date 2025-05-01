package cgb.transfert.entities;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.*;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Customer {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;
    private String name;
    private String address;
    private String lei;

    @OneToMany(mappedBy = "owner", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference("customer_accounts")
    private Set<Account> accounts = new HashSet<>();

    @OneToMany(mappedBy = "customer", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference("customer_users")
    private Set<UserCGB> userCGBS = new HashSet<>();

    // Comptes bénéficiaires (ManyToMany)
    @ManyToMany
    @JoinTable(
            name = "customer_beneficiaryaccounts",
            joinColumns = @JoinColumn(name = "customer_id"),
            inverseJoinColumns = @JoinColumn(name = "account_iban")
    )
    @JsonManagedReference("beneficiary_accounts")
    private Set<Account> beneficiaryAccounts = new HashSet<>();
}
