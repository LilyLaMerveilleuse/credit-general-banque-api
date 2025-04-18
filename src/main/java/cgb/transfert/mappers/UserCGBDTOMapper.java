package cgb.transfert.mappers;

import cgb.transfert.dtos.UserCGBDTO;
import cgb.transfert.entities.UserCGB;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class UserCGBDTOMapper {

    public UserCGBDTO toDTO(UserCGB userCGB) {
        return new UserCGBDTO(userCGB);
    }

    public List<UserCGBDTO> toDTO(List<UserCGB> userCGBs) {
        List<UserCGBDTO> dtos = new ArrayList<>();

        for (UserCGB userCGB : userCGBs) {
            dtos.add(new UserCGBDTO(userCGB));
        }

        return dtos;
    }
}
