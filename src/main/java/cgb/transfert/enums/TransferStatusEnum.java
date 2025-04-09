package cgb.transfert.enums;

import lombok.Getter;

@Getter
public enum TransferStatusEnum {
    NEW("Transfer créé"),
    ERROR("Erreur de transfer"),
    CANCELLED("Transfer annulé"),
    DONE("Tranfer complété");

    private final String label;

    TransferStatusEnum(String label) {
        this.label = label;
    }

}
