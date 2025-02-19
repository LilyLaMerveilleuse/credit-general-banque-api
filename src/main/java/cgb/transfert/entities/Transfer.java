package cgb.transfert.entities;

import jakarta.persistence.*;
import java.time.LocalDate;
import lombok.*;

@Entity
@Data
@Table(name="Transfer")
public class Transfer {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
	private String sourceAccountNumber;
    private String destinationAccountNumber;
    private Double amount;
    private LocalDate transferDate;
    private String description;
}