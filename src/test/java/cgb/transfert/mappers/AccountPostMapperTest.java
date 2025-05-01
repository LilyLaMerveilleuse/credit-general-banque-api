package cgb.transfert.mappers;

import cgb.transfert.entities.Account;
import cgb.transfert.records.AccountPostRecord;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class AccountPostMapperTest {

    private AccountPostMapper accountPostMapper;

    @BeforeEach
    void setUp() {
        accountPostMapper = new AccountPostMapper();
    }

    @Test
    void toEntity_ShouldMapFieldsCorrectly() {
        // Given
        AccountPostRecord record = new AccountPostRecord("ACC000111", "Alice Wonderland", 2000.0);

        // When
        Account account = accountPostMapper.toEntity(record);

        // Then
        assertThat(account).isNotNull();
        assertThat(account.getIban()).isEqualTo("ACC000111");
        assertThat(account.getOwner_name()).isEqualTo("Alice Wonderland");
        assertThat(account.getSolde()).isEqualTo(2000.0);
    }
}
