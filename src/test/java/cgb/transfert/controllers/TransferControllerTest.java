package cgb.transfert.controllers;

import cgb.transfert.entities.Account;
import cgb.transfert.entities.Transfer;
import cgb.transfert.entities.TransferStatus;
import cgb.transfert.records.TransferPostRecord;
import cgb.transfert.services.TransferService;
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

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
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

    private final ObjectMapper objectMapper = new ObjectMapper();

    private UUID transferId;
    private Account source;
    private Account destination;
    private Transfer transfer;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(transferController).build();

        source = new Account("SRC123", "John", 1000.0);
        destination = new Account("DST456", "Jane", 500.0);
        transferId = UUID.randomUUID();
        transfer = new Transfer(transferId, 200.0, LocalDate.now(), "Payment",
                new TransferStatus(UUID.randomUUID(), "DONE"), source, destination);
    }

    @Test
    void getAllTransfers_ShouldReturnList() throws Exception {
        when(transferService.getAllTransfers()).thenReturn(List.of(transfer));

        mockMvc.perform(get("/api/transfers"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(transferId.toString()))
                .andExpect(jsonPath("$[0].amount").value(200.0));
    }

    @Test
    void getTransferById_ShouldReturnTransfer_WhenFound() throws Exception {
        when(transferService.getTransferById(transferId)).thenReturn(Optional.of(transfer));

        mockMvc.perform(get("/api/transfers/" + transferId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(transferId.toString()))
                .andExpect(jsonPath("$.amount").value(200.0));
    }

    @Test
    void getTransferById_ShouldReturnNotFound_WhenNotExists() throws Exception {
        when(transferService.getTransferById(transferId)).thenReturn(Optional.empty());

        mockMvc.perform(get("/api/transfers/" + transferId))
                .andExpect(status().isNotFound());
    }

    @Test
    void createTransfer_ShouldReturnCreatedTransfer() throws Exception {
        TransferPostRecord post = new TransferPostRecord(200.0, "Payment", "SRC123", "DST456");
        when(transferService.saveTransfer(any())).thenReturn(transfer);

        mockMvc.perform(post("/api/transfers")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(post)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.amount").value(200.0))
                .andExpect(jsonPath("$.description").value("Payment"));
    }

    @Test
    void deleteTransfer_ShouldReturnOk() throws Exception {
        doNothing().when(transferService).deleteTransfer(transferId);

        mockMvc.perform(delete("/api/transfers/" + transferId))
                .andExpect(status().isOk());
    }

    @Test
    void getTransfersBySourceAccount_ShouldReturnList() throws Exception {
        when(transferService.getTransfersBySourceAccountNumber("SRC123")).thenReturn(List.of(transfer));

        mockMvc.perform(get("/api/transfers/source/SRC123"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].sourceAccount.iban").value("SRC123"));
    }

    @Test
    void getTransfersByDestinationAccount_ShouldReturnList() throws Exception {
        when(transferService.getTransfersByDestinationAccountNumber("DST456")).thenReturn(List.of(transfer));

        mockMvc.perform(get("/api/transfers/destination/DST456"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].destinationAccount.iban").value("DST456"));
    }
}
