package cgb.transfert.validators;
import cgb.transfert.annotations.ValidIban;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.util.Locale;

public class IbanValidator implements ConstraintValidator<ValidIban, String> {

    @Override
    public boolean isValid(String iban, ConstraintValidatorContext context) {
        if (iban == null || iban.isBlank()) {
            return false;
        }

        String trimmed = iban.replaceAll("\\s+", "").toUpperCase(Locale.ROOT);
        if (!trimmed.matches("^[A-Z]{2}\\d{2}[A-Z0-9]{11,30}$")) {
            return false;
        }

        // IBAN checksum validation
        String reformatted = trimmed.substring(4) + trimmed.substring(0, 4);
        StringBuilder numericIban = new StringBuilder();

        for (char c : reformatted.toCharArray()) {
            int value = Character.isLetter(c) ? c - 'A' + 10 : Character.getNumericValue(c);
            numericIban.append(value);
        }

        try {
            return new java.math.BigInteger(numericIban.toString()).mod(java.math.BigInteger.valueOf(97)).intValue() == 1;
        } catch (NumberFormatException e) {
            return false;
        }
    }
}
