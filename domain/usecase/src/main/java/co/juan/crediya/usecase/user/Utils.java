package co.juan.crediya.usecase.user;

import java.math.BigDecimal;

public class Utils {

    private static final BigDecimal MIN = BigDecimal.ZERO;
    private static final BigDecimal MAX = new BigDecimal("15000000");

    public static boolean validateMoney(BigDecimal val) {
        return validateMoney(MIN, MAX, val);
    }

    public static boolean validateMoney(BigDecimal min, BigDecimal max, BigDecimal value) {
        return value != null && value.compareTo(min) >= 0 && value.compareTo(max) <= 0;
    }
}
