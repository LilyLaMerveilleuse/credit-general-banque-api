package cgb.transfert.controllers;

import cgb.transfert.entities.Account;
import cgb.transfert.records.AccountPostRecord;
import cgb.transfert.services.AccountService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Arrays;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
class AccountControllerTest {

    @Mock
    private AccountService accountService;

    @InjectMocks
    private AccountController accountController;

    private MockMvc mockMvc;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(accountController).build();
    }

    @Test
    void getAllAccounts_ShouldReturnListOfAccounts() throws Exception {
        Account account1 = new Account("ACC123456", 1000.0);
        Account account2 = new Account("ACC654321", 500.0);

        when(accountService.getAllAccounts()).thenReturn(Arrays.asList(account1, account2));

        mockMvc.perform(get("/api/accounts"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].accountNumber").value("ACC123456"))
                .andExpect(jsonPath("$[1].accountNumber").value("ACC654321"));
    }

    @Test
    void getAccountByNumber_ShouldReturnAccount_WhenFound() throws Exception {
        Account account = new Account("ACC123456", 1000.0);
        when(accountService.getAccountByNumber("ACC123456")).thenReturn(Optional.of(account));

        mockMvc.perform(get("/api/accounts/ACC123456"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.accountNumber").value("ACC123456"))
                .andExpect(jsonPath("$.solde").value(1000.0));
    }

    @Test
    void getAccountByNumber_ShouldReturnNotFound_WhenAccountDoesNotExist() throws Exception {
        when(accountService.getAccountByNumber("ACC999999")).thenReturn(Optional.empty());

        mockMvc.perform(get("/api/accounts/ACC999999"))
                .andExpect(status().isNotFound());
    }

    @Test
    void createAccount_ShouldReturnCreatedAccount() throws Exception {
        AccountPostRecord accountPostRecord = new AccountPostRecord(1500.0);
        Account savedAccount = new Account("ACC111111", 1500.0);

        when(accountService.saveAccount(any(AccountPostRecord.class))).thenReturn(savedAccount);

        mockMvc.perform(post("/api/accounts")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(accountPostRecord)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.accountNumber").value("ACC111111"))
                .andExpect(jsonPath("$.solde").value(1500.0));
    }

    @Test
    void deleteAccount_ShouldReturnOk() throws Exception {
        Mockito.doNothing().when(accountService).deleteAccount("ACC123456");

        mockMvc.perform(delete("/api/accounts/ACC123456"))
                .andExpect(status().isOk());
    }
}
