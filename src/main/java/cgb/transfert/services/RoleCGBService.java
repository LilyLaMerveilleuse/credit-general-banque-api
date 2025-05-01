package cgb.transfert.services;

import cgb.transfert.entities.RoleCGB;
import cgb.transfert.repositories.RoleCGBRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class RoleCGBService {

    private final RoleCGBRepository roleCGBRepository;

    @Autowired
    public RoleCGBService(RoleCGBRepository roleCGBRepository) {
        this.roleCGBRepository = roleCGBRepository;
    }

    public List<RoleCGB> getAllRoles() {
        return roleCGBRepository.findAll();
    }

    public Optional<RoleCGB> getRoleById(UUID roleId) {
        return roleCGBRepository.findById(roleId);
    }

    public void deleteRole(UUID roleId) {
        roleCGBRepository.deleteById(roleId);
    }
}
