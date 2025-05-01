package cgb.transfert.records;

import java.util.UUID;

public record UserCGBPostRecord(String username, String password, UUID role_id, UUID customer_id) {
}
