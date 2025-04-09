package cgb.transfert.mappers;

import cgb.transfert.entities.Account;
import cgb.transfert.entities.Transfer;
import cgb.transfert.entities.TransferStatus;
import cgb.transfert.enums.TransferStatusEnum;
import cgb.transfert.records.TransferPostRecord;
import cgb.transfert.repositories.AccountRepository;
import cgb.transfert.repositories.TransferStatusRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class TransferPostMapperTest {

    private TransferPostMapper mapper;

    @Mock
    private AccountRepository accountRepository;
    @Mock
    private TransferStatusRepository transferStatusRepository;

    private Account source;
    private Account destination;
    private TransferStatus status;

    @BeforeEach
    void setUp() {
        mapper = new TransferPostMapper();
        accountRepository = mock(AccountRepository.class);
        transferStatusRepository = mock(TransferStatusRepository.class);
        ReflectionTestUtils.setField(mapper, "accountRepository", accountRepository);
        ReflectionTestUtils.setField(mapper, "transferStatusRepository", transferStatusRepository);

        source = new Account("SRC123", "John", 1000.0);
        destination = new Account("DST456", "Jane", 500.0);
        status = new TransferStatus(UUID.randomUUID(), TransferStatusEnum.NEW.getLabel());
    }

    @Test
    void toEntity_ShouldMapRecordToTransfer() {
        TransferPostRecord post = new TransferPostRecord(100.0, "Test", "SRC123", "DST456");

        when(accountRepository.findById("SRC123")).thenReturn(Optional.of(source));
        when(accountRepository.findById("DST456")).thenReturn(Optional.of(destination));
        when(transferStatusRepository.findByName(TransferStatusEnum.NEW.getLabel())).thenReturn(Optional.of(status));

        Transfer transfer = mapper.toEntity(post);

        assertThat(transfer.getAmount()).isEqualTo(100.0);
        assertThat(transfer.getDescription()).isEqualTo("Test");
        assertThat(transfer.getSourceAccount()).isEqualTo(source);
        assertThat(transfer.getDestinationAccount()).isEqualTo(destination);
        assertThat(transfer.getStatus()).isEqualTo(status);
    }
}

