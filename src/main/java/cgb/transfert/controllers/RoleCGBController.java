package cgb.transfert.controllers;

import cgb.transfert.dtos.RoleCGBDTO;
import cgb.transfert.entities.RoleCGB;
import cgb.transfert.mappers.RoleCGBDTOMapper;
import cgb.transfert.services.RoleCGBService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/api/Role")
public class RoleCGBController {

    private final RoleCGBService roleCGBService;
    private final RoleCGBDTOMapper roleCGBDTOMapper;

    @Autowired
    public RoleCGBController(RoleCGBService roleCGBService, RoleCGBDTOMapper roleCGBDTOMapper) {
        this.roleCGBService = roleCGBService;
        this.roleCGBDTOMapper = roleCGBDTOMapper;
    }

    @GetMapping
    public List<RoleCGBDTO> getAllRolees() {
        return roleCGBDTOMapper.toDTO(
                roleCGBService.getAllRoles()
        );
    }

    @GetMapping("/{id}")
    public RoleCGBDTO getRoleById(@PathVariable UUID id) {
        Optional<RoleCGB> role = roleCGBService.getRoleById(id);
        if (role.isPresent()) {
            return roleCGBDTOMapper.toDTO(
                    role.get()
            );
        } else {
            throw new EntityNotFoundException("User not found");
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteRole(@PathVariable UUID id) {
        roleCGBService.deleteRole(id);
        return ResponseEntity.ok().build();
    }
}
