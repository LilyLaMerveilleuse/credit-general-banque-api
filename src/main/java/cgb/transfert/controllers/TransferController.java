package cgb.transfert.controllers;

import cgb.transfert.entities.Transfer;
import cgb.transfert.services.TransferService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/transfers")
public class TransferController {

    private final TransferService transferService;

    @Autowired
    public TransferController(TransferService transferService) {
        this.transferService = transferService;
    }

    @GetMapping
    public List<Transfer> getAllTransfers() {
        return transferService.getAllTransfers();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Transfer> getTransferById(@PathVariable Long id) {
        Optional<Transfer> transfer = transferService.getTransferById(id);
        return transfer.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    public Transfer createTransfer(@RequestBody Transfer transfer) {
        return transferService.saveTransfer(transfer);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTransfer(@PathVariable Long id) {
        transferService.deleteTransfer(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/source/{accountNumber}")
    public List<Transfer> getTransfersBySourceAccount(@PathVariable String accountNumber) {
        return transferService.getTransfersBySourceAccount(accountNumber);
    }

    @GetMapping("/destination/{accountNumber}")
    public List<Transfer> getTransfersByDestinationAccount(@PathVariable String accountNumber) {
        return transferService.getTransfersByDestinationAccount(accountNumber);
    }
}
