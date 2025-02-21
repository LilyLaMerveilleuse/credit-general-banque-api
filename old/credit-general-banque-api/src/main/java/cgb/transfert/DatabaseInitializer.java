package cgb.transfert;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import jakarta.annotation.PostConstruct;

@Component
public class DatabaseInitializer {

    @Autowired
    private AccountRepository accountRepository;

    @PostConstruct
    public void init() {
        // Vérifiez si la base de données est vide avant d'insérer des données
        if (accountRepository.count() == 0) {
           insertSampleData(accountRepository);
        }
    }

    public static void insertSampleData(AccountRepository accountRepository) {
        // Insérer des comptes d'exemple
        Account account1 = new Account();
        account1.setAccountNumber("123456789");
        account1.setSolde(300.00);
        accountRepository.save(account1);

        Account account2 = new Account();
        account2.setAccountNumber("987654321");
        account2.setSolde(500.00);
        accountRepository.save(account2);

        Account account3 = new Account();
        account3.setAccountNumber("456789123");
        account3.setSolde(2000.00);
        accountRepository.save(account3);
    }
}
