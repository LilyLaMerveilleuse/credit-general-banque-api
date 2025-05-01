package cgb.transfert.services;

import cgb.transfert.entities.*;
import cgb.transfert.enums.TransferStatusEnum;
import cgb.transfert.mappers.TransferPostMapper;
import cgb.transfert.records.CreatedLot;
import cgb.transfert.records.TransferLotPostRecord;
import cgb.transfert.records.TransferLotUnitRecord;
import cgb.transfert.records.TransferPostRecord;
import cgb.transfert.repositories.AccountRepository;
import cgb.transfert.repositories.TransferRepository;
import jakarta.persistence.EntityExistsException;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;

@Service
public class TransferService {

    private final TransferRepository transferRepository;
    private final AccountRepository accountRepository;
    private final TransferStatusService transferStatusService;
    private final CurrentUserService currentUserService;
    private final AsyncTransferService asyncTransferService;


    @Autowired
    public TransferService(TransferRepository transferRepository,
                           AccountRepository accountRepository, TransferStatusService transferStatusService, CurrentUserService currentUserService, AsyncTransferService asyncTransferService) {
        this.transferRepository = transferRepository;
        this.accountRepository = accountRepository;
        this.transferStatusService = transferStatusService;
        this.currentUserService = currentUserService;
        this.asyncTransferService = asyncTransferService;
    }

    public List<Transfer> getAllTransfers() {
        List<Transfer> transfers = transferRepository.findAll();
        List<Transfer> transfersToReturn = new ArrayList<>();
        for (Transfer transfer : transfers) {
            if (userOwnsTransfer(transfer) || SecurityContextService.isAdmin()) {
                transfersToReturn.add(transfer);
            }
        }
        return transfersToReturn;
    }

    public Optional<Transfer> getTransferById(UUID id) {
        return transferRepository.findById(id);
    }

    public Transfer saveTransfer(Transfer transfer) {
        if (!userOwnsTransfer(transfer) && !SecurityContextService.isAdmin()) {
            throw new RuntimeException("Cet utilisateur ne possède pas le compte source");
        }
        Account sourceAccount = transfer.getSourceAccount();
        Account destinationAccount = transfer.getDestinationAccount();
        if (sourceAccount.getSolde() >= transfer.getAmount()) {
            if (sourceAccount.getOwner().getBeneficiaryAccounts().contains(destinationAccount)) {
                sourceAccount.setSolde(sourceAccount.getSolde() - transfer.getAmount());
                destinationAccount.setSolde(destinationAccount.getSolde() + transfer.getAmount());
                Optional<TransferStatus> status = transferStatusService.getTransferStatusByName(TransferStatusEnum.DONE.getLabel());
                transfer.setStatus(status.orElseThrow());
            } else {
                Optional<TransferStatus> status = transferStatusService.getTransferStatusByName(TransferStatusEnum.UNAUTHORIZED.getLabel());
                transfer.setStatus(status.orElseThrow());
            }
        } else {
            Optional<TransferStatus> status = transferStatusService.getTransferStatusByName(TransferStatusEnum.ERROR.getLabel());
            transfer.setStatus(status.orElseThrow());
        }
        return transferRepository.save(transfer);
    }

    public List<TransferPostRecord> formatTransfers(TransferLotPostRecord transfers) {
        List<TransferPostRecord> transferPostRecords = new ArrayList<>();
        for (TransferLotUnitRecord transfer : transfers.transfers()){
            transferPostRecords.add(
                    new TransferPostRecord(
                            transfer.amount(),
                            transfer.description(),
                            transfers.iban(),
                            transfer.iban()
                    )
            );
        }
        return transferPostRecords;
    }

    public CreatedLot saveTransfers(List<Transfer> transfers){
        UUID id = UUID.randomUUID();
        UserCGB user = currentUserService.getCurrentUser();
        // Force le chargement des données chargées en lazy pour la méthode async
        for (Transfer transfer : transfers) {
            transfer.getSourceAccount().getOwner().getBeneficiaryAccounts().size();
        }
        user.getCustomer().getAccounts().size();

        asyncTransferService.saveTransfersAsync(transfers, id, user);
        return new CreatedLot(id, LocalDateTime.now(), "Traitement en cours", "enCours");
    }

    public void deleteTransfer(UUID id) {
        transferRepository.deleteById(id);
    }

    public List<Transfer> getTransfersBySourceAccountNumber(String sourceAccountNumber) {
        Optional<Account> accountOptional = accountRepository.findById(sourceAccountNumber);
        if (accountOptional.isPresent()) {
            return transferRepository.findBySourceAccount(accountOptional.get());
        } else {
            throw new EntityExistsException("Le compte n°" + sourceAccountNumber + " n'as pas été trouvé.");
        }
    }

    public List<Transfer> getTransfersByDestinationAccountNumber(String destinationAccountNumber) {
        Optional<Account> accountOptional = accountRepository.findById(destinationAccountNumber);
        if (accountOptional.isPresent()) {
            return transferRepository.findByDestinationAccount(accountOptional.get());
        } else {
            throw new EntityExistsException("Le compte n°" + destinationAccountNumber + " n'as pas été trouvé.");
        }
    }

    public List<Transfer> getTransfersByLotId(UUID lotId) {
        if (!userOwnsLot(lotId)) {throw new RuntimeException("Cet utilisateur ne possède pas ce lot!");}
        return transferRepository.findByLotId(lotId);
    }

    public boolean userOwnsTransfer(Transfer transfer) {
        UserCGB user = currentUserService.getCurrentUser();
        Set<Customer> customerList = new HashSet<>();
        customerList.addAll(transfer.getSourceAccount().getSourceCustomers());
        customerList.addAll(transfer.getDestinationAccount().getSourceCustomers());
        Set<UserCGB> userList = new HashSet<>();
        for (Customer customer : customerList) {
            userList.addAll(customer.getUserCGBS());
        }
        if (userList.contains(user)) {
            return true;
        }
        return false;
    }

    public boolean userOwnsLot(UUID lotId) {
        if (lotId == null) {return false;}
        UserCGB user = currentUserService.getCurrentUser();
        Set<Customer> customerList = new HashSet<>();
        List<Transfer> transfers = transferRepository.findByLotId(lotId);
        for (Transfer transfer : transfers) {
            for (Customer customer : transfer.getSourceAccount().getSourceCustomers()) {
                customerList.add(customer);
            }
        }
        Set<UserCGB> userList = new HashSet<>();
        for (Customer customer : customerList) {
            userList.addAll(customer.getUserCGBS());
        }
        if (userList.contains(user)) {
            return true;
        }
        return false;
    }
}
