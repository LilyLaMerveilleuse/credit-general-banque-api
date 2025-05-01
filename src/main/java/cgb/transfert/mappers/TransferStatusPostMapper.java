package cgb.transfert.mappers;

import cgb.transfert.entities.TransferStatus;
import cgb.transfert.records.TransferStatusPostRecord;
import org.springframework.stereotype.Component;

@Component
public class TransferStatusPostMapper {

    public TransferStatus toEntity(TransferStatusPostRecord transferStatusPostRecord) {
        TransferStatus transferStatus = new TransferStatus();
        transferStatus.setName(transferStatusPostRecord.name());
        return transferStatus;
    }
}
