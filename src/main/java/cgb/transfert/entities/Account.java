package cgb.transfert.entities;

import cgb.transfert.annotations.ValidIban;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
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
    private String owner_name;
	private Double solde;

    @ManyToMany
    @JoinTable(
            name = "Beneficiaire",
            joinColumns = @JoinColumn(name = "iban_account"),
            inverseJoinColumns = @JoinColumn(name = "iban_beneficiaire")
    )
    @JsonManagedReference
    private Set<Account> beneficiaires = new HashSet<>();
}