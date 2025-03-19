package cgb.transfert.records;

import java.time.LocalDate;

public record TransferPostRecord(Double amount, LocalDate transferDate, String description, String sourceAccountNumber, String destinationAccountNumber) {
}
