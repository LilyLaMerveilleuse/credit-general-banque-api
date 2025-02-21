package cgb.transfert.controllers;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;

import cgb.transfert.entities.Account;
import cgb.transfert.records.AccountPostRecord;
import cgb.transfert.services.AccountService;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.List;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
public class AccountControllerTest {

    private MockMvc mockMvc;

    @Mock
    private AccountService accountService;

    @InjectMocks
    private AccountController accountController;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @BeforeEach
    void setup() {
        mockMvc = MockMvcBuilders.standaloneSetup(accountController).build();
    }

    @Test
    public void testGetAllAccounts() throws Exception {
        List<Account> accounts = List.of(
                new Account("ACC123456", 1000.0),
                new Account("ACC654321", 2000.0)
        );

        when(accountService.getAllAccounts()).thenReturn(accounts);

        mockMvc.perform(get("/api/accounts"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()").value(2))
                .andExpect(jsonPath("$[0].accountNumber").value("ACC123456"))
                .andExpect(jsonPath("$[1].accountNumber").value("ACC654321"))
                .andDo(print());
    }

    @Test
    public void testGetAccountByNumber_Exists() throws Exception {
        Account account = new Account("ACC123456", 1000.0);
        when(accountService.getAccountByNumber("ACC123456")).thenReturn(Optional.of(account));

        mockMvc.perform(get("/api/accounts/ACC123456"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.accountNumber").value("ACC123456"))
                .andExpect(jsonPath("$.solde").value(1000.0))
                .andDo(print());
    }

    @Test
    public void testGetAccountByNumber_NotFound() throws Exception {
        when(accountService.getAccountByNumber("ACC999999")).thenReturn(Optional.empty());

        mockMvc.perform(get("/api/accounts/ACC999999"))
                .andExpect(status().isNotFound())
                .andDo(print());
    }

    @Test
    public void testCreateAccount() throws Exception {
        AccountPostRecord accountPostRecord = new AccountPostRecord(1500.0);
        Account createdAccount = new Account("ACC123456", 1500.0);

        when(accountService.saveAccount(any(AccountPostRecord.class))).thenReturn(createdAccount);

        mockMvc.perform(post("/api/accounts")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(accountPostRecord)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.accountNumber").value("ACC123456"))
                .andExpect(jsonPath("$.solde").value(1500.0))
                .andDo(print());
    }

    @Test
    public void testDeleteAccount() throws Exception {
        doNothing().when(accountService).deleteAccount("ACC123456");

        mockMvc.perform(delete("/api/accounts/ACC123456"))
                .andExpect(status().isOk())
                .andDo(print());

        verify(accountService, times(1)).deleteAccount("ACC123456");
    }
}
