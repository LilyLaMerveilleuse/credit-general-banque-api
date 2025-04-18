package cgb.transfert.mappers;

import cgb.transfert.dtos.RoleCGBDTO;
import cgb.transfert.entities.RoleCGB;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class RoleCGBDTOMapper {

    public RoleCGBDTO toDTO(RoleCGB roleCGB) {
        return new RoleCGBDTO(roleCGB);
    }

    public List<RoleCGBDTO> toDTO(List<RoleCGB> roleCGBs) {
        List<RoleCGBDTO> dtos = new ArrayList<>();

        for (RoleCGB roleCGB : roleCGBs) {
            dtos.add(new RoleCGBDTO(roleCGB));
        }

        return dtos;
    }
}
