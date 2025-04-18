package cgb.transfert.services;

import cgb.transfert.entities.UserCGB;
import cgb.transfert.repositories.UserCGBRepository;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class CurrentUserService {

    private final UserCGBRepository userRepository;

    public CurrentUserService(UserCGBRepository userRepository) {
        this.userRepository = userRepository;
    }

    public UserCGB getCurrentUser() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null || !auth.isAuthenticated()) {
            throw new RuntimeException("Aucun utilisateur connecté");
        }

        String username = auth.getName();
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("Utilisateur non trouvé"));
    }
}
