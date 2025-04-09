package cgb.transfert.services;

import cgb.transfert.entities.Account;
import cgb.transfert.entities.Transfer;
import cgb.transfert.entities.TransferStatus;
import cgb.transfert.enums.TransferStatusEnum;
import cgb.transfert.mappers.TransferPostMapper;
import cgb.transfert.records.TransferPostRecord;
import cgb.transfert.repositories.AccountRepository;
import cgb.transfert.repositories.TransferRepository;
import jakarta.persistence.EntityExistsException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TransferServiceTest {

    @Mock private TransferRepository transferRepository;
    @Mock private TransferPostMapper transferPostMapper;
    @Mock private AccountRepository accountRepository;
    @Mock private TransferStatusService transferStatusService;

    @InjectMocks
    private TransferService transferService;

    private Account source;
    private Account destination;
    private Transfer transfer;
    private TransferPostRecord postRecord;
    private UUID transferId;

    @BeforeEach
    void setUp() {
        source = new Account("SRC123", "John", 1000.0);
        destination = new Account("DST456", "Jane", 500.0);
        postRecord = new TransferPostRecord(200.0, "Test Transfer", "SRC123", "DST456");
        transfer = new Transfer(UUID.randomUUID(), 200.0, LocalDate.now(), "Test Transfer",
                new TransferStatus(UUID.randomUUID(), "NEW"), source, destination);
        transferId = transfer.getId();
    }

    @Test
    void saveTransfer_ShouldUpdateBalancesAndSetStatus() {
        when(transferPostMapper.toEntity(postRecord)).thenReturn(transfer);
        when(transferStatusService.getTransferStatusByName(TransferStatusEnum.DONE.getLabel()))
                .thenReturn(Optional.of(new TransferStatus(UUID.randomUUID(), TransferStatusEnum.DONE.getLabel())));
        when(transferRepository.save(any())).thenReturn(transfer);

        Transfer saved = transferService.saveTransfer(postRecord);

        assertThat(saved).isNotNull();
        assertThat(saved.getStatus().getName()).isEqualTo(TransferStatusEnum.DONE.getLabel());
        assertThat(source.getSolde()).isEqualTo(800.0);
        assertThat(destination.getSolde()).isEqualTo(700.0);
    }

    @Test
    void saveTransfer_ShouldCancelTransfer_WhenInsufficientFunds() {
        source.setSolde(50.0);
        when(transferPostMapper.toEntity(postRecord)).thenReturn(transfer);
        when(transferStatusService.getTransferStatusByName(TransferStatusEnum.CANCELLED.getLabel()))
                .thenReturn(Optional.of(new TransferStatus(UUID.randomUUID(), TransferStatusEnum.CANCELLED.getLabel())));
        when(transferRepository.save(any())).thenReturn(transfer);

        Transfer saved = transferService.saveTransfer(postRecord);

        assertThat(saved.getStatus().getName()).isEqualTo(TransferStatusEnum.CANCELLED.getLabel());
    }

    @Test
    void getTransferById_ShouldReturnTransfer_WhenExists() {
        when(transferRepository.findById(transferId)).thenReturn(Optional.of(transfer));
        Optional<Transfer> found = transferService.getTransferById(transferId);

        assertThat(found).isPresent();
        assertThat(found.get().getDescription()).isEqualTo("Test Transfer");
    }

    @Test
    void getAllTransfers_ShouldReturnList() {
        when(transferRepository.findAll()).thenReturn(List.of(transfer));
        assertThat(transferService.getAllTransfers()).hasSize(1);
    }

    @Test
    void deleteTransfer_ShouldCallRepository() {
        transferService.deleteTransfer(transferId);
        verify(transferRepository).deleteById(transferId);
    }

    @Test
    void getTransfersBySourceAccountNumber_ShouldReturnTransfers() {
        when(accountRepository.findById("SRC123")).thenReturn(Optional.of(source));
        when(transferRepository.findBySourceAccount(source)).thenReturn(List.of(transfer));

        List<Transfer> transfers = transferService.getTransfersBySourceAccountNumber("SRC123");

        assertThat(transfers).hasSize(1);
    }

    @Test
    void getTransfersByDestinationAccountNumber_ShouldReturnTransfers() {
        when(accountRepository.findById("DST456")).thenReturn(Optional.of(destination));
        when(transferRepository.findByDestinationAccount(destination)).thenReturn(List.of(transfer));

        List<Transfer> transfers = transferService.getTransfersByDestinationAccountNumber("DST456");

        assertThat(transfers).hasSize(1);
    }
}
