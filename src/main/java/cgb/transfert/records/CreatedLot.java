package cgb.transfert.records;

import cgb.transfert.entities.TransferStatus;

import java.time.LocalDateTime;
import java.util.UUID;

public record CreatedLot(UUID numLot, LocalDateTime dateLancement, String message, String etat) {
}
