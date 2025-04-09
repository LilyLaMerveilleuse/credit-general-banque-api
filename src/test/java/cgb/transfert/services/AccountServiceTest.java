package cgb.transfert.services;

import cgb.transfert.entities.Account;
import cgb.transfert.mappers.AccountPostMapper;
import cgb.transfert.records.AccountPostRecord;
import cgb.transfert.repositories.AccountRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AccountServiceTest {

    @Mock
    private AccountRepository accountRepository;

    @Mock
    private AccountPostMapper accountPostMapper;

    @InjectMocks
    private AccountService accountService;

    private Account account;
    private AccountPostRecord accountPostRecord;

    @BeforeEach
    void setUp() {
        account = new Account("ACC123456", "John Doe", 500.0);
        accountPostRecord = new AccountPostRecord("ACC123456", "John Doe", 500.0);
    }

    @Test
    void getAllAccounts_ShouldReturnAllAccounts() {
        when(accountRepository.findAll()).thenReturn(List.of(account));

        List<Account> accounts = accountService.getAllAccounts();

        assertThat(accounts).isNotEmpty().hasSize(1);
        assertThat(accounts.getFirst().getIban()).isEqualTo("ACC123456");
        verify(accountRepository, times(1)).findAll();
    }

    @Test
    void getAccountByNumber_ShouldReturnAccount_WhenAccountExists() {
        when(accountRepository.findById("ACC123456")).thenReturn(Optional.of(account));

        Optional<Account> foundAccount = accountService.getAccountByNumber("ACC123456");

        assertThat(foundAccount).isPresent();
        assertThat(foundAccount.get().getSolde()).isEqualTo(500.0);
        verify(accountRepository, times(1)).findById("ACC123456");
    }

    @Test
    void getAccountByNumber_ShouldReturnEmpty_WhenAccountDoesNotExist() {
        when(accountRepository.findById("ACC999999")).thenReturn(Optional.empty());

        Optional<Account> foundAccount = accountService.getAccountByNumber("ACC999999");

        assertThat(foundAccount).isEmpty();
        verify(accountRepository, times(1)).findById("ACC999999");
    }

    @Test
    void saveAccount_ShouldReturnSavedAccount() {
        when(accountPostMapper.toEntity(any(AccountPostRecord.class))).thenReturn(account);
        when(accountRepository.save(any(Account.class))).thenReturn(account);

        Account savedAccount = accountService.saveAccount(accountPostRecord);

        assertThat(savedAccount).isNotNull();
        assertThat(savedAccount.getIban()).isEqualTo("ACC123456");
        verify(accountPostMapper, times(1)).toEntity(any(AccountPostRecord.class));
        verify(accountRepository, times(1)).save(any(Account.class));
    }

    @Test
    void deleteAccount_ShouldDeleteAccount() {
        doNothing().when(accountRepository).deleteById("ACC123456");

        accountService.deleteAccount("ACC123456");

        verify(accountRepository, times(1)).deleteById("ACC123456");
    }
}
