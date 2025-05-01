package cgb.transfert.controllers;

import cgb.transfert.dtos.TransferStatusDTO;
import cgb.transfert.entities.Transfer;
import cgb.transfert.entities.TransferStatus;
import cgb.transfert.mappers.TransferStatusDTOMapper;
import cgb.transfert.records.TransferPostRecord;
import cgb.transfert.records.TransferStatusPostRecord;
import cgb.transfert.services.TransferService;
import cgb.transfert.services.TransferStatusService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/api/transferstatus")
public class TransferStatusController {

    private final TransferStatusService transferStatusService;
    private final TransferStatusDTOMapper transferStatusDTOMapper;

    @Autowired
    public TransferStatusController(TransferStatusService transferStatusService, TransferStatusDTOMapper transferStatusDTOMapper) {
        this.transferStatusService = transferStatusService;
        this.transferStatusDTOMapper = transferStatusDTOMapper;
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping
    public List<TransferStatusDTO> getAllTransferStatuses() {
        return transferStatusDTOMapper.toDTO(
                transferStatusService.getAllTransferStatuses()
        );
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping("/{id}")
    public TransferStatusDTO getTransferStatusById(@PathVariable UUID id) {
        Optional<TransferStatus> transferStatus = transferStatusService.getTransferStatusById(id);
        if (transferStatus.isPresent()) {
            return transferStatusDTOMapper.toDTO(
                    transferStatus.get()
            );
        } else {
            throw new EntityNotFoundException("Transfer status not found");
        }
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PostMapping
    public TransferStatusDTO createTransferStatus(@RequestBody TransferStatusPostRecord transferStatus) {
        return transferStatusDTOMapper.toDTO(
                transferStatusService.saveTransferStatus(transferStatus)
        );
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTransferStatus(@PathVariable UUID id) {
        transferStatusService.deleteTransferStatus(id);
        return ResponseEntity.ok().build();
    }
}
