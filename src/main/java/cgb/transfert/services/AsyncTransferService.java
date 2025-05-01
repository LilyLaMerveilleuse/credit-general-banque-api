package cgb.transfert.services;

import cgb.transfert.entities.Account;
import cgb.transfert.entities.Transfer;
import cgb.transfert.entities.TransferStatus;
import cgb.transfert.entities.UserCGB;
import cgb.transfert.enums.TransferStatusEnum;
import cgb.transfert.handlers.LoggingInterceptor;
import cgb.transfert.mappers.TransferPostMapper;
import cgb.transfert.records.TransferLotPostRecord;
import cgb.transfert.records.TransferLotUnitRecord;
import cgb.transfert.records.TransferPostRecord;
import cgb.transfert.repositories.AccountRepository;
import cgb.transfert.repositories.TransferRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.AsyncResult;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.concurrent.CompletableFuture;

@Service
public class AsyncTransferService {
    private final TransferRepository transferRepository;
    private final TransferStatusService transferStatusService;
    private final EmailService emailService;

    private static final Logger logger = LoggerFactory.getLogger(LoggingInterceptor.class);

    public AsyncTransferService(TransferRepository transferRepository, TransferStatusService transferStatusService, EmailService emailService) {
        this.transferRepository = transferRepository;
        this.transferStatusService = transferStatusService;
        this.emailService = emailService;
    }

    @Async
    public CompletableFuture<Transfer> saveTransferAsync(Transfer transfer, UserCGB user) {
        if (!user.getCustomer().getAccounts().contains(transfer.getSourceAccount())) {
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
        return CompletableFuture.completedFuture(transferRepository.save(transfer));
    }

    @Async
    public void saveTransfersAsync(List<Transfer> transfers, UUID uuid, UserCGB user) {
        List<Transfer> savedTransfers = new ArrayList<>();
        for (Transfer transfer : transfers) {
            transfer.setLotId(uuid);
            Transfer savedTransfer = saveTransferAsync(transfer, user).join();
            savedTransfers.add(savedTransfer);
        }
        int nbTransfersReussi = 0;
        for (Transfer savedTransfer : savedTransfers) {
            if (Objects.equals(savedTransfer.getStatus().getName(), TransferStatusEnum.DONE.getLabel())){
                nbTransfersReussi++;
            }
        }
        emailService.sendEmail(user.getUsername(), "Envoi par lot N°" + uuid.toString(),
                "L'envoi par lot est terminé! Il a effectué " + savedTransfers.size() +
                " transfers. " + nbTransfersReussi + " transfers ont été effectués avec succès!");
        logger.info("Transfer par lot N°"+uuid.toString()+" effectué");
    }
}
