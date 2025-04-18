package cgb.transfert.controllers;

import cgb.transfert.dtos.UserCGBDTO;
import cgb.transfert.entities.UserCGB;
import cgb.transfert.mappers.UserCGBDTOMapper;
import cgb.transfert.mappers.UserCGBPostMapper;
import cgb.transfert.records.UserCGBPostRecord;
import cgb.transfert.services.UserCGBService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/api/UserCGB")
public class UserCGBController {

    private final UserCGBService userCGBService;
    private final UserCGBDTOMapper userCGBDTOMapper;
    private final UserCGBPostMapper userCGBPostMapper;

    @Autowired
    public UserCGBController(UserCGBService userCGBService, UserCGBDTOMapper userCGBDTOMapper, UserCGBPostMapper userCGBPostMapper) {
        this.userCGBService = userCGBService;
        this.userCGBDTOMapper = userCGBDTOMapper;
        this.userCGBPostMapper = userCGBPostMapper;
    }

    @GetMapping
    public List<UserCGBDTO> getAllUserCGBes() {
        return userCGBDTOMapper.toDTO(
                userCGBService.getAllUserCGBs()
        );
    }

    @GetMapping("/{id}")
    public UserCGBDTO getUserCGBById(@PathVariable UUID id) {
        Optional<UserCGB> userCGB = userCGBService.getUserCGBById(id);
        if (userCGB.isPresent()) {
            return userCGBDTOMapper.toDTO(
                    userCGB.get()
            );
        } else {
            throw new EntityNotFoundException("User not found");
        }
    }

    @PostMapping
    public UserCGBDTO createUserCGB(@RequestBody UserCGBPostRecord userCGB) {
        return userCGBDTOMapper.toDTO(
                userCGBService.saveUserCGB(
                        userCGBPostMapper.toEntity(userCGB)
                )
        );
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUserCGB(@PathVariable UUID id) {
        userCGBService.deleteUserCGB(id);
        return ResponseEntity.ok().build();
    }
}
