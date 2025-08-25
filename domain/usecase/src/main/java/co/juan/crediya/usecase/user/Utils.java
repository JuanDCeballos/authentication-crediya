package co.juan.crediya.usecase.user;

import java.math.BigDecimal;
import java.util.regex.Pattern;

public class Utils {

    private static final String EMAIL_PATTERN =
            "^(?=.{1,64}@)[A-Za-z0-9_-]+(\\.[A-Za-z0-9_-]+)*@[^-][A-Za-z0-9-]+(\\.[A-Za-z0-9-]+)*(\\.[A-Za-z]{2,})$";

    public static boolean validateField(String field) {
        return field != null && !field.isBlank();
    }

    public static boolean validateEmail(String email) {
        if (!validateField(email)) {
            return false;
        }

        return Pattern.compile(EMAIL_PATTERN)
                .matcher(email)
                .matches();
    }

    public static boolean validateMoney(BigDecimal val) {
        return validateMoney(BigDecimal.ZERO, new BigDecimal("15000000"), val);
    }

    public static boolean validateMoney(BigDecimal min, BigDecimal max, BigDecimal value) {
        return value != null && value.compareTo(min) >= 0 && value.compareTo(max) <= 0;
    }
}
