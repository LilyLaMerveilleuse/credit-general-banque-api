package cgb.transfert.services;

import cgb.transfert.entities.UserCGB;
import cgb.transfert.mappers.UserCGBPostMapper;
import cgb.transfert.records.UserCGBPostRecord;
import cgb.transfert.repositories.UserCGBRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class UserCGBService {

    private final UserCGBRepository userCGBRepository;
    private final UserCGBPostMapper userCGBPostMapper;

    @Autowired
    public UserCGBService(UserCGBRepository userCGBRepository, UserCGBPostMapper userCGBPostMapper) {
        this.userCGBRepository = userCGBRepository;
        this.userCGBPostMapper = userCGBPostMapper;
    }

    public List<UserCGB> getAllUserCGBs() {
        return userCGBRepository.findAll();
    }

    public Optional<UserCGB> getUserCGBById(UUID userCGBId) {
        return userCGBRepository.findById(userCGBId);
    }

    public UserCGB saveUserCGB(UserCGB userCGB) {
        return userCGBRepository.save(userCGB);
    }

    public void deleteUserCGB(UUID userCGBId) {
        userCGBRepository.deleteById(userCGBId);
    }
}
