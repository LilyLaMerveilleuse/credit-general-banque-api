package cgb.transfert.controllers;

import cgb.transfert.entities.Account;
import cgb.transfert.entities.Transfer;
import cgb.transfert.records.TransferPostRecord;
import cgb.transfert.services.TransferService;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
class TransferControllerTest {

    @Mock
    private TransferService transferService;

    @InjectMocks
    private TransferController transferController;

    private MockMvc mockMvc;

    private ObjectMapper objectMapper = new ObjectMapper();

    private Account sourceAccount;
    private Account destinationAccount;
    private Transfer transfer;
    private TransferPostRecord transferPostRecord;

    @BeforeEach
    void setUp() {
        objectMapper.registerModule(new JavaTimeModule());
        mockMvc = MockMvcBuilders.standaloneSetup(transferController).build();

        sourceAccount = new Account("ACC123456", 5000.0);
        destinationAccount = new Account("ACC654321", 2000.0);

        transfer = new Transfer(1L, 1000.0, LocalDate.now(), "Test Transfer", sourceAccount, destinationAccount);
        transferPostRecord = new TransferPostRecord(1000.0, LocalDate.now(), "Test Transfer", "ACC123456", "ACC654321");
    }

    @Test
    void getAllTransfers_ShouldReturnListOfTransfers() throws Exception {
        when(transferService.getAllTransfers()).thenReturn(Arrays.asList(transfer));

        mockMvc.perform(get("/api/transfers"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].amount").value(1000.0));
    }

    @Test
    void getTransferById_ShouldReturnTransfer_WhenFound() throws Exception {
        when(transferService.getTransferById(1L)).thenReturn(Optional.of(transfer));

        mockMvc.perform(get("/api/transfers/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.amount").value(1000.0));
    }

    @Test
    void getTransferById_ShouldReturnNotFound_WhenTransferDoesNotExist() throws Exception {
        when(transferService.getTransferById(99L)).thenReturn(Optional.empty());

        mockMvc.perform(get("/api/transfers/99"))
                .andExpect(status().isNotFound());
    }

    @Test
    void createTransfer_ShouldReturnCreatedTransfer() throws Exception {
        when(transferService.saveTransfer(any(TransferPostRecord.class))).thenReturn(transfer);

        mockMvc.perform(post("/api/transfers")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(transferPostRecord)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.amount").value(1000.0));
    }

    @Test
    void deleteTransfer_ShouldReturnOk() throws Exception {
        Mockito.doNothing().when(transferService).deleteTransfer(1L);

        mockMvc.perform(delete("/api/transfers/1"))
                .andExpect(status().isOk());
    }

    @Test
    void getTransfersBySourceAccountNumber_ShouldReturnTransfers() throws Exception {
        when(transferService.getTransfersBySourceAccountNumber("ACC123456")).thenReturn(Arrays.asList(transfer));

        mockMvc.perform(get("/api/transfers/source/ACC123456"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].amount").value(1000.0));
    }

    @Test
    void getTransfersByDestinationAccount_ShouldReturnTransfers() throws Exception {
        when(transferService.getTransfersByDestinationAccountNumber("ACC654321")).thenReturn(Arrays.asList(transfer));

        mockMvc.perform(get("/api/transfers/destination/ACC654321"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].amount").value(1000.0));
    }
}
