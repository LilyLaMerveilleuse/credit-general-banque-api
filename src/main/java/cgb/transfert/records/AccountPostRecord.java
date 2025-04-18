package cgb.transfert.records;

import java.util.UUID;

public record AccountPostRecord(String iban, Double solde, UUID customerId) {
}
