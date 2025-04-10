package cgb.transfert.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TransferStatusDTO {
    private UUID id;
    private String name;

    public TransferStatusDTO(cgb.transfert.entities.TransferStatus status) {
        this.id = status.getId();
        this.name = status.getName();
    }
}