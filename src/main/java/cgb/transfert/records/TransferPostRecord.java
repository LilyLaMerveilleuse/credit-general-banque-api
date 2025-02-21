package cgb.transfert.records;

import java.time.LocalDate;

public record TransferPostRecord(Double amount, String sourceAccountNumber, String destinationAccountNumber, LocalDate transferDate, String description) {
}
