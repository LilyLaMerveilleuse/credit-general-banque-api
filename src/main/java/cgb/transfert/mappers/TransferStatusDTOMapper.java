package cgb.transfert.mappers;

import cgb.transfert.dtos.TransferStatusDTO;
import cgb.transfert.entities.TransferStatus;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class TransferStatusDTOMapper {
    public TransferStatusDTO toDTO(TransferStatus transferStatus) {
        return new TransferStatusDTO(transferStatus);
    }

    public List<TransferStatusDTO> toDTO(List<TransferStatus> transferStatuses) {
        List<TransferStatusDTO> transferStatusDTOs = new ArrayList<>();

        for (TransferStatus transferStatus : transferStatuses) {
            transferStatusDTOs.add(toDTO(transferStatus));
        }

        return transferStatusDTOs;
    }
}
