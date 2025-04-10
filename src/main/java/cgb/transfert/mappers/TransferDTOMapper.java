package cgb.transfert.mappers;

import cgb.transfert.dtos.TransferDTO;
import cgb.transfert.entities.Transfer;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class TransferDTOMapper {
    public TransferDTO toDTO(Transfer transfer) {
        return new TransferDTO(transfer);
    }

    public List<TransferDTO> toDTO(List<Transfer> transfers) {
        List<TransferDTO> dtos = new ArrayList<>();

        for (Transfer transfer : transfers) {
            dtos.add(new TransferDTO(transfer));
        }

        return dtos;
    }
}
