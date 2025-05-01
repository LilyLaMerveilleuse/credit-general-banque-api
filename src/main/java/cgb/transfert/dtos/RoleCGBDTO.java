package cgb.transfert.dtos;

import cgb.transfert.entities.RoleCGB;
import lombok.Getter;

import java.util.UUID;

@Getter
public class RoleCGBDTO {
    private UUID id;
    private String name;

    public RoleCGBDTO(RoleCGB roleCGB) {
        this.id = roleCGB.getId();
        this.name = roleCGB.getName();
    }
}
