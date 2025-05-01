package cgb.transfert.records;

import java.util.List;

public record TransferLotPostRecord(String iban, List<TransferLotUnitRecord> transfers) {
}
