package cgb.transfert.entities;

import jakarta.persistence.*;
import java.time.LocalDate;
import lombok.*;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name="Transfer")
public class Transfer {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Double amount;
    private LocalDate transferDate;
    private String description;

    @ManyToOne
    @JoinColumn(name = "sourceAccountNumber", nullable = false)
    private Account sourceAccount;

    @ManyToOne
    @JoinColumn(name = "destinationAccountNumber", nullable = false)
    private Account destinationAccount;
}