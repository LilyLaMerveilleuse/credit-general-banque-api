package cgb.transfert.services;

import cgb.transfert.entities.Account;
import cgb.transfert.entities.Transfer;
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
        sourceAccount = new Account("ACC123456", 5000.0);
        destinationAccount = new Account("ACC654321", 2000.0);
        transfer = new Transfer(1L, 1000.0, LocalDate.now(), "Test Transfer", sourceAccount, destinationAccount);
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
        when(transferRepository.findById(1L)).thenReturn(Optional.of(transfer));

        Optional<Transfer> foundTransfer = transferService.getTransferById(1L);

        assertThat(foundTransfer).isPresent();
        assertThat(foundTransfer.get().getAmount()).isEqualTo(1000.0);
        verify(transferRepository, times(1)).findById(1L);
    }

    @Test
    void getTransferById_ShouldReturnEmpty_WhenNotFound() {
        when(transferRepository.findById(99L)).thenReturn(Optional.empty());

        Optional<Transfer> foundTransfer = transferService.getTransferById(99L);

        assertThat(foundTransfer).isEmpty();
        verify(transferRepository, times(1)).findById(99L);
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
        doNothing().when(transferRepository).deleteById(1L);

        transferService.deleteTransfer(1L);

        verify(transferRepository, times(1)).deleteById(1L);
    }

    @Test
    void getTransfersBySourceAccountNumber_ShouldReturnTransfers_WhenAccountExists() {
        when(accountRepository.findById("ACC123456")).thenReturn(Optional.of(sourceAccount));
        when(transferRepository.findBySourceAccount(sourceAccount)).thenReturn(List.of(transfer));

        List<Transfer> transfers = transferService.getTransfersBySourceAccountNumber("ACC123456");

        assertThat(transfers).isNotEmpty().hasSize(1);
        verify(accountRepository, times(1)).findById("ACC123456");
        verify(transferRepository, times(1)).findBySourceAccount(sourceAccount);
    }

    @Test
    void getTransfersBySourceAccountNumber_ShouldThrowException_WhenAccountDoesNotExist() {
        when(accountRepository.findById("ACC999999")).thenReturn(Optional.empty());

        assertThatThrownBy(() -> transferService.getTransfersBySourceAccountNumber("ACC999999"))
                .isInstanceOf(EntityExistsException.class)
                .hasMessage("Le compte n°ACC999999 n'as pas été trouvé.");

        verify(accountRepository, times(1)).findById("ACC999999");
        verifyNoInteractions(transferRepository);
    }

    @Test
    void getTransfersByDestinationAccountNumber_ShouldReturnTransfers_WhenAccountExists() {
        when(accountRepository.findById("ACC654321")).thenReturn(Optional.of(destinationAccount));
        when(transferRepository.findByDestinationAccount(destinationAccount)).thenReturn(List.of(transfer));

        List<Transfer> transfers = transferService.getTransfersByDestinationAccountNumber("ACC654321");

        assertThat(transfers).isNotEmpty().hasSize(1);
        verify(accountRepository, times(1)).findById("ACC654321");
        verify(transferRepository, times(1)).findByDestinationAccount(destinationAccount);
    }

    @Test
    void getTransfersByDestinationAccountNumber_ShouldThrowException_WhenAccountDoesNotExist() {
        when(accountRepository.findById("ACC999999")).thenReturn(Optional.empty());

        assertThatThrownBy(() -> transferService.getTransfersByDestinationAccountNumber("ACC999999"))
                .isInstanceOf(EntityExistsException.class)
                .hasMessage("Le compte n°ACC999999 n'as pas été trouvé.");

        verify(accountRepository, times(1)).findById("ACC999999");
        verifyNoInteractions(transferRepository);
    }
}
