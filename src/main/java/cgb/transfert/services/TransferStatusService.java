package cgb.transfert.services;

import cgb.transfert.entities.TransferStatus;
import cgb.transfert.mappers.TransferStatusPostMapper;
import cgb.transfert.records.TransferStatusPostRecord;
import cgb.transfert.repositories.TransferStatusRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class TransferStatusService {

    TransferStatusRepository transferStatusRepository;
    TransferStatusPostMapper transferStatusPostMapper;

    @Autowired
    public TransferStatusService(TransferStatusRepository transferStatusRepository, TransferStatusPostMapper transferStatusPostMapper) {
        this.transferStatusRepository = transferStatusRepository;
        this.transferStatusPostMapper = transferStatusPostMapper;
    }

    public List<TransferStatus> getAllTransferStatuses() {
        return transferStatusRepository.findAll();
    }

    public Optional<TransferStatus> getTransferStatusById(UUID id) {
        return transferStatusRepository.findById(id);
    }

    public Optional<TransferStatus> getTransferStatusByName(String name) {
        return transferStatusRepository.findByName(name);
    }

    public TransferStatus saveTransferStatus(TransferStatusPostRecord transferStatusPostRecord) {
        return transferStatusRepository.save(transferStatusPostMapper.toEntity(transferStatusPostRecord));
    }

    public void deleteTransferStatus(UUID id) {
        transferStatusRepository.deleteById(id);
    }
}
