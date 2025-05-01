package cgb.transfert.controllers;

import cgb.transfert.dtos.TransferDTO;
import cgb.transfert.entities.Transfer;
import cgb.transfert.mappers.TransferDTOMapper;
import cgb.transfert.mappers.TransferPostMapper;
import cgb.transfert.records.CreatedLot;
import cgb.transfert.records.TransferLotPostRecord;
import cgb.transfert.records.TransferPostRecord;
import cgb.transfert.services.CurrentUserService;
import cgb.transfert.services.SecurityContextService;
import cgb.transfert.services.TransferService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
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

    @PreAuthorize("hasAnyRole('ROLE_COMPTABLE', 'ROLE_ADMIN', 'ROLE_USER')")
    @GetMapping
    public List<TransferDTO> getAllTransfers() {
        return transferDTOMapper.toDTO(
                transferService.getAllTransfers()
        );
    }

    @PreAuthorize("hasAnyRole('ROLE_COMPTABLE', 'ROLE_ADMIN')")
    @GetMapping("/{id}")
    public TransferDTO getTransferById(@PathVariable UUID id) {
        Optional<Transfer> transfer = transferService.getTransferById(id);
        if (transfer.isPresent() && (SecurityContextService.isAdmin() || transferService.userOwnsTransfer(transfer.get()))) {
            return transferDTOMapper.toDTO(
                    transfer.get()
            );
        } else {
            throw new EntityNotFoundException("Le transfer n°" + id + " n'a pas été trouvé");
        }
    }

    @PreAuthorize("hasAnyRole('ROLE_COMPTABLE', 'ROLE_ADMIN')")
    @PostMapping
    public TransferDTO createTransfer(@RequestBody TransferPostRecord transfer) {
        return transferDTOMapper.toDTO(
                transferService.saveTransfer(
                        transferPostMapper.toEntity(transfer)
                )
        );
    }

    @PreAuthorize("hasAnyRole('ROLE_COMPTABLE', 'ROLE_ADMIN')")
    @PostMapping("/lot")
    public CreatedLot createMultiTransfer(@RequestBody TransferLotPostRecord transfers) {
        List<TransferPostRecord> formatedTransfers = transferService.formatTransfers(transfers);
        return transferService.saveTransfers(
                transferPostMapper.toEntities(formatedTransfers)
        );
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTransfer(@PathVariable UUID id) {
        transferService.deleteTransfer(id);
        return ResponseEntity.ok().build();
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping("/source/{accountNumber}")
    public List<TransferDTO> getTransfersBySourceAccountNumber(@PathVariable String accountNumber) {
        return transferDTOMapper.toDTO(
                transferService.getTransfersBySourceAccountNumber(accountNumber)
        );
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping("/destination/{accountNumber}")
    public List<TransferDTO> getTransfersByDestinationAccount(@PathVariable String accountNumber) {
        return transferDTOMapper.toDTO(
                transferService.getTransfersByDestinationAccountNumber(accountNumber)
        );
    }

    @PreAuthorize("hasAnyRole('ROLE_COMPTABLE', 'ROLE_ADMIN')")
    @GetMapping("/lot/{id}")
    public List<TransferDTO> getTransfersByLotId(@PathVariable UUID id) {
        return transferDTOMapper.toDTO(transferService.getTransfersByLotId(id));
    }
}
