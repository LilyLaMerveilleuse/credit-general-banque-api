package cgb.transfert.controllers;

import cgb.transfert.dtos.AccountDTO;
import cgb.transfert.entities.Account;
import cgb.transfert.mappers.AccountDTOMapper;
import cgb.transfert.records.AccountPostRecord;
import cgb.transfert.services.AccountService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/accounts")
public class AccountController {

    private final AccountService accountService;
    private final AccountDTOMapper accountDTOMapper;

    @Autowired
    public AccountController(AccountService accountService, AccountDTOMapper accountDTOMapper) {
        this.accountService = accountService;
        this.accountDTOMapper = accountDTOMapper;
    }

    @GetMapping
    public List<AccountDTO> getAllAccounts() {
        return accountDTOMapper.toDTO(
                accountService.getAllAccounts()
        );
    }

    @GetMapping("/{accountNumber}")
    public AccountDTO getAccountByNumber(@PathVariable String accountNumber) {
        Optional<Account> account = accountService.getAccountByNumber(accountNumber);
        if (account.isPresent()) {
            return accountDTOMapper.toDTO(
                    account.get()
            );
        } else {
            throw new EntityNotFoundException("Le compte n°" + accountNumber + " n'a pas été trouvé");
        }
    }

    @PostMapping
    public AccountDTO createAccount(@RequestBody AccountPostRecord account) {
        return accountDTOMapper.toDTO(
                accountService.saveAccount(account)
        );
    }

    @DeleteMapping("/{accountNumber}")
    public ResponseEntity<Void> deleteAccount(@PathVariable String accountNumber) {
        accountService.deleteAccount(accountNumber);
        return ResponseEntity.ok().build();
    }
}
