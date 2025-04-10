package cgb.transfert.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TransferDTO {
    private UUID id;
    private Double amount;
    private LocalDate transferDate;
    private String description;
    private UUID lotId;
    private TransferStatusDTO status;
    private String sourceIban;
    private String destinationIban;

    public TransferDTO(cgb.transfert.entities.Transfer transfer) {
        this.id = transfer.getId();
        this.amount = transfer.getAmount();
        this.transferDate = transfer.getTransfer_date();
        this.description = transfer.getDescription();
        this.lotId = transfer.getLotId();
        this.status = transfer.getStatus() != null ? new TransferStatusDTO(transfer.getStatus()) : null;
        this.sourceIban = transfer.getSourceAccount() != null ? transfer.getSourceAccount().getIban() : null;
        this.destinationIban = transfer.getDestinationAccount() != null ? transfer.getDestinationAccount().getIban() : null;
    }
}
