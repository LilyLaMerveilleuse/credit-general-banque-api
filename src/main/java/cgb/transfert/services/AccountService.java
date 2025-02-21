package cgb.transfert.services;

import cgb.transfert.entities.Account;
import cgb.transfert.mappers.AccountPostMapper;
import cgb.transfert.records.AccountPostRecord;
import cgb.transfert.repositories.AccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.Random;

@Service
public class AccountService {

    private final AccountRepository accountRepository;
    private final AccountPostMapper accountPostMapper;

    @Autowired
    public AccountService(AccountRepository accountRepository, AccountPostMapper accountPostMapper) {
        this.accountRepository = accountRepository;
        this.accountPostMapper = accountPostMapper;
    }

    public List<Account> getAllAccounts() {
        return accountRepository.findAll();
    }

    public Optional<Account> getAccountByNumber(String accountNumber) {
        return accountRepository.findById(accountNumber);
    }

    public Account saveAccount(AccountPostRecord accountPost) {
        return accountRepository.save(accountPostMapper.toEntity(accountPost));
    }

    public void deleteAccount(String accountNumber) {
        accountRepository.deleteById(accountNumber);
    }
}
