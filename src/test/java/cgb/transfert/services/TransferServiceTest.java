package cgb.transfert.services;

import cgb.transfert.entities.Account;
import cgb.transfert.entities.Transfer;
import cgb.transfert.entities.TransferStatus;
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

    @Mock
    private TransferRepository transferRepository;

    @Mock
    private TransferPostMapper transferPostMapper;

    @Mock
    private AccountRepository accountRepository;

    @InjectMocks
    private TransferService transferService;

    private Account sourceAccount;
    private Account destinationAccount;
    private Transfer transfer;
    private TransferPostRecord transferPostRecord;

    @BeforeEach
    void setUp() {
        Account sourceAccount = new Account("FR7630006000010000000000001", "Alice Dupont", 5000.00);
        Account destinationAccount = new Account("FR7630006000010000000000019", "Sophie Fabre", 2000.00);

        transfer = new Transfer(UUID.fromString("1a2b3c4d-0001-0001-0001-000000000001"), 1000.0, LocalDate.now(), "Test Transfer", new TransferStatus(), sourceAccount, destinationAccount);
        transferPostRecord = new TransferPostRecord(1000.0, LocalDate.now(), "Test Transfer", "ACC123456", "ACC654321");
    }

    @Test
    void getAllTransfers_ShouldReturnAllTransfers() {
        when(transferRepository.findAll()).thenReturn(List.of(transfer));

        List<Transfer> transfers = transferService.getAllTransfers();

        assertThat(transfers).isNotEmpty().hasSize(1);
        assertThat(transfers.getFirst().getAmount()).isEqualTo(1000.0);
        verify(transferRepository, times(1)).findAll();
    }

    @Test
    void getTransferById_ShouldReturnTransfer_WhenFound() {
        when(transferRepository.findById(UUID.fromString("1a2b3c4d-0001-0001-0001-000000000001"))).thenReturn(Optional.of(transfer));

        Optional<Transfer> foundTransfer = transferService.getTransferById(UUID.fromString("1a2b3c4d-0001-0001-0001-000000000001"));

        assertThat(foundTransfer).isPresent();
        assertThat(foundTransfer.get().getAmount()).isEqualTo(1000.0);
        verify(transferRepository, times(1)).findById(UUID.fromString("1a2b3c4d-0001-0001-0001-000000000001"));
    }

    @Test
    void getTransferById_ShouldReturnEmpty_WhenNotFound() {
        when(transferRepository.findById(UUID.fromString("1a2b3c4d-0001-0001-0001-000000099999"))).thenReturn(Optional.empty());

        Optional<Transfer> foundTransfer = transferService.getTransferById(UUID.fromString("1a2b3c4d-0001-0001-0001-000000099999"));

        assertThat(foundTransfer).isEmpty();
        verify(transferRepository, times(1)).findById(UUID.fromString("1a2b3c4d-0001-0001-0001-000000099999"));
    }

    @Test
    void saveTransfer_ShouldReturnSavedTransfer() {
        when(transferPostMapper.toEntity(any(TransferPostRecord.class))).thenReturn(transfer);
        when(transferRepository.save(any(Transfer.class))).thenReturn(transfer);

        Transfer savedTransfer = transferService.saveTransfer(transferPostRecord);

        assertThat(savedTransfer).isNotNull();
        assertThat(savedTransfer.getAmount()).isEqualTo(1000.0);
        verify(transferPostMapper, times(1)).toEntity(any(TransferPostRecord.class));
        verify(transferRepository, times(1)).save(any(Transfer.class));
    }

    @Test
    void deleteTransfer_ShouldDeleteTransfer() {
        doNothing().when(transferRepository).deleteById(UUID.fromString("1a2b3c4d-0001-0001-0001-000000000001"));

        transferService.deleteTransfer(UUID.fromString("1a2b3c4d-0001-0001-0001-000000000001"));

        verify(transferRepository, times(1)).deleteById(UUID.fromString("1a2b3c4d-0001-0001-0001-000000000001"));
    }

    @Test
    void getTransfersBySourceAccountNumber_ShouldReturnTransfers_WhenAccountExists() {
        when(accountRepository.findById("FR7630006000010000000000001")).thenReturn(Optional.of(sourceAccount));
        when(transferRepository.findBySourceAccount(sourceAccount)).thenReturn(List.of(transfer));

        List<Transfer> transfers = transferService.getTransfersBySourceAccountNumber("FR7630006000010000000000001");

        assertThat(transfers).isNotEmpty().hasSize(1);
        verify(accountRepository, times(1)).findById("FR7630006000010000000000001");
        verify(transferRepository, times(1)).findBySourceAccount(sourceAccount);
    }

    @Test
    void getTransfersBySourceAccountNumber_ShouldThrowException_WhenAccountDoesNotExist() {
        when(accountRepository.findById("FR7630006000010000000099999")).thenReturn(Optional.empty());

        assertThatThrownBy(() -> transferService.getTransfersBySourceAccountNumber("FR7630006000010000000099999"))
                .isInstanceOf(EntityExistsException.class)
                .hasMessage("Le compte n°FR7630006000010000000099999 n'as pas été trouvé.");

        verify(accountRepository, times(1)).findById("FR7630006000010000000099999");
        verifyNoInteractions(transferRepository);
    }

    @Test
    void getTransfersByDestinationAccountNumber_ShouldReturnTransfers_WhenAccountExists() {
        when(accountRepository.findById("FR7630006000010000000000019")).thenReturn(Optional.of(destinationAccount));
        when(transferRepository.findByDestinationAccount(destinationAccount)).thenReturn(List.of(transfer));

        List<Transfer> transfers = transferService.getTransfersByDestinationAccountNumber("FR7630006000010000000000019");

        assertThat(transfers).isNotEmpty().hasSize(1);
        verify(accountRepository, times(1)).findById("FR7630006000010000000000019");
        verify(transferRepository, times(1)).findByDestinationAccount(destinationAccount);
    }

    @Test
    void getTransfersByDestinationAccountNumber_ShouldThrowException_WhenAccountDoesNotExist() {
        when(accountRepository.findById("FR7630006000010000000099999")).thenReturn(Optional.empty());

        assertThatThrownBy(() -> transferService.getTransfersByDestinationAccountNumber("FR7630006000010000000099999"))
                .isInstanceOf(EntityExistsException.class)
                .hasMessage("Le compte n°FR7630006000010000000099999 n'as pas été trouvé.");

        verify(accountRepository, times(1)).findById("FR7630006000010000000099999");
        verifyNoInteractions(transferRepository);
    }
}
