package cgb.transfert.records;

public record TransferPostRecord(Double amount, String description, String ibanSource, String ibanDestination) {
}
