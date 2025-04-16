package cgb.transfert.entities;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.UUID;

import lombok.*;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name="Transfer")
public class Transfer {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;
    private Double amount;
    private LocalDateTime transfer_date;
    private String description;

    @Column(name = "lot_id")
    private UUID lotId;

    @ManyToOne
    @JoinColumn(name = "status_id", nullable = false)
    private TransferStatus status;

    @ManyToOne
    @JoinColumn(name = "source_iban", nullable = false)
    private Account sourceAccount;

    @ManyToOne
    @JoinColumn(name = "destination_iban", nullable = false)
    private Account destinationAccount;
}