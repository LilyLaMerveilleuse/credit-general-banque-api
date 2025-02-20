package cgb.transfert.entities;

import jakarta.persistence.*;
import lombok.Data;

import java.util.Collection;

@Entity
@Data
public class Account {
    @Id
    private String accountNumber;
	private Double solde;
}