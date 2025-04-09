package cgb.transfert.controllers;

import cgb.transfert.entities.Transfer;
import cgb.transfert.entities.TransferStatus;
import cgb.transfert.records.TransferPostRecord;
import cgb.transfert.records.TransferStatusPostRecord;
import cgb.transfert.services.TransferService;
import cgb.transfert.services.TransferStatusService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/api/transferstatus")
public class TransferStatusController {

    private final TransferStatusService transferStatusService;

    @Autowired
    public TransferStatusController(TransferStatusService transferStatusService) {
        this.transferStatusService = transferStatusService;
    }

    @GetMapping
    public List<TransferStatus> getAllTransferStatuses() {
        return transferStatusService.getAllTransferStatuses();
    }

    @GetMapping("/{id}")
    public ResponseEntity<TransferStatus> getTransferStatusById(@PathVariable UUID id) {
        Optional<TransferStatus> transferStatus = transferStatusService.getTransferStatusById(id);
        return transferStatus.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    public TransferStatus createTransferStatus(@RequestBody TransferStatusPostRecord transferStatus) {
        return transferStatusService.saveTransferStatus(transferStatus);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTransferStatus(@PathVariable UUID id) {
        transferStatusService.deleteTransferStatus(id);
        return ResponseEntity.ok().build();
    }
}
