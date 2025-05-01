package cgb.transfert.dtos;

import cgb.transfert.entities.UserCGB;
import lombok.Getter;

import java.util.UUID;

@Getter
public class UserCGBDTO {
    private UUID id;
    private String username;
    private String password;
    private RoleCGBDTO rolecgb;

    public UserCGBDTO(UserCGB userCGB) {
        this.id = userCGB.getId();
        this.username = userCGB.getUsername();
        this.password = userCGB.getPassword();
        this.rolecgb = new RoleCGBDTO(userCGB.getRoleCGB());
    }
}
