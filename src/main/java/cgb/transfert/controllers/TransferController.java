package cgb.transfert.controllers;

import cgb.transfert.dtos.TransferDTO;
import cgb.transfert.entities.Transfer;
import cgb.transfert.mappers.TransferDTOMapper;
import cgb.transfert.mappers.TransferPostMapper;
import cgb.transfert.records.TransferLotPostRecord;
import cgb.transfert.records.TransferPostRecord;
import cgb.transfert.services.TransferService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/api/transfers")
public class TransferController {

    private final TransferService transferService;
    private final TransferDTOMapper transferDTOMapper;
    private final TransferPostMapper transferPostMapper;

    @Autowired
    public TransferController(TransferService transferService, TransferDTOMapper transferDTOMapper, TransferPostMapper transferPostMapper) {
        this.transferService = transferService;
        this.transferDTOMapper = transferDTOMapper;
        this.transferPostMapper = transferPostMapper;
    }

    @GetMapping
    public List<TransferDTO> getAllTransfers() {
        return transferDTOMapper.toDTO(
                transferService.getAllTransfers()
        );
    }

    @GetMapping("/{id}")
    public TransferDTO getTransferById(@PathVariable UUID id) {
        Optional<Transfer> transfer = transferService.getTransferById(id);
        if (transfer.isPresent()) {
            return transferDTOMapper.toDTO(
                    transfer.get()
            );
        } else {
            throw new EntityNotFoundException("Le transfer n°" + id + " n'a pas été trouvé");
        }
    }

    @PostMapping
    public TransferDTO createTransfer(@RequestBody TransferPostRecord transfer) {
        return transferDTOMapper.toDTO(
                transferService.saveTransfer(
                        transferPostMapper.toEntity(transfer)
                )
        );
    }

    @PostMapping("/lot")
    public List<TransferDTO> createMultiTransfer(@RequestBody TransferLotPostRecord transfers) {
        List<TransferPostRecord> formatedTransfers = transferService.formatTransfers(transfers);
        return transferDTOMapper.toDTO(
                transferService.saveTransfers(
                        transferPostMapper.toEntities(formatedTransfers)
                )
        );
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTransfer(@PathVariable UUID id) {
        transferService.deleteTransfer(id);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/source/{accountNumber}")
    public List<TransferDTO> getTransfersBySourceAccountNumber(@PathVariable String accountNumber) {
        return transferDTOMapper.toDTO(
                transferService.getTransfersBySourceAccountNumber(accountNumber)
        );
    }

    @GetMapping("/destination/{accountNumber}")
    public List<TransferDTO> getTransfersByDestinationAccount(@PathVariable String accountNumber) {
        return transferDTOMapper.toDTO(
                transferService.getTransfersByDestinationAccountNumber(accountNumber)
        );
    }

    @GetMapping("/lot/{id}")
    public List<TransferDTO> getTransfersByLotId(@PathVariable UUID id) {
        return transferDTOMapper.toDTO(transferService.getTransfersByLotId(id));
    }
}
