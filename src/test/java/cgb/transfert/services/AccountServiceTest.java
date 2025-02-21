package cgb.transfert.services;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

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

@ExtendWith(MockitoExtension.class)
public class AccountServiceTest {

    @Mock
    private AccountRepository accountRepository;

    @Mock
    private AccountPostMapper accountPostMapper;

    @InjectMocks
    private AccountService accountService;

    private Account account;
    private AccountPostRecord accountPostRecord;

    @BeforeEach
    void setup() {
        account = new Account("ACC123456", 1500.0);
        accountPostRecord = new AccountPostRecord(1500.0);
    }

    @Test
    public void testGetAllAccounts() {
        when(accountRepository.findAll()).thenReturn(List.of(account));

        List<Account> accounts = accountService.getAllAccounts();

        assertEquals(1, accounts.size());
        assertEquals("ACC123456", accounts.getFirst().getAccountNumber());
        verify(accountRepository, times(1)).findAll();
    }

    @Test
    public void testGetAccountByNumber_Found() {
        when(accountRepository.findById("ACC123456")).thenReturn(Optional.of(account));

        Optional<Account> foundAccount = accountService.getAccountByNumber("ACC123456");

        assertTrue(foundAccount.isPresent());
        assertEquals("ACC123456", foundAccount.get().getAccountNumber());
        verify(accountRepository, times(1)).findById("ACC123456");
    }

    @Test
    public void testGetAccountByNumber_NotFound() {
        when(accountRepository.findById("ACC999999")).thenReturn(Optional.empty());

        Optional<Account> foundAccount = accountService.getAccountByNumber("ACC999999");

        assertFalse(foundAccount.isPresent());
        verify(accountRepository, times(1)).findById("ACC999999");
    }

    @Test
    public void testSaveAccount() {
        Account mappedAccount = new Account("ACC654321", 2000.0);

        when(accountPostMapper.toEntity(accountPostRecord)).thenReturn(mappedAccount);
        when(accountRepository.save(mappedAccount)).thenReturn(mappedAccount);

        Account savedAccount = accountService.saveAccount(accountPostRecord);

        assertEquals("ACC654321", savedAccount.getAccountNumber());
        assertEquals(2000.0, savedAccount.getSolde());
        verify(accountPostMapper, times(1)).toEntity(accountPostRecord);
        verify(accountRepository, times(1)).save(mappedAccount);
    }

    @Test
    public void testDeleteAccount() {
        doNothing().when(accountRepository).deleteById("ACC123456");

        accountService.deleteAccount("ACC123456");

        verify(accountRepository, times(1)).deleteById("ACC123456");
    }
}
