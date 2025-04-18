package cgb.transfert.entities;

import cgb.transfert.annotations.ValidIban;
import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.*;

import java.util.HashSet;
import java.util.Set;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = "iban")
public class Account {
    @Id
    @Column(name = "iban", nullable = false)
    @ValidIban
    private String iban;
	private Double solde;

    // Compte géré par un seul client
    @ManyToOne
    @JoinColumn(name = "owner_id")
    @JsonBackReference("customer_accounts")
    private Customer owner;

    // Ce compte peut être bénéficiaire de plusieurs clients
    @ManyToMany(mappedBy = "beneficiaryAccounts")
    @JsonBackReference("beneficiary_accounts")
    private Set<Customer> sourceCustomers = new HashSet<>();
}