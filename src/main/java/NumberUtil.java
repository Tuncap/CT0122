
public class NumberUtil {
    public static int convertPercentToInt(String value) {
        String tempPercent = value.replace("%", "");
        if (isNumeric(tempPercent)) {
            return Integer.parseInt(tempPercent);
        }
        throw new NumberFormatException(String.format("%s is not a valid number", tempPercent));
    }

    private static boolean isNumeric(String str) {
        return str.matches("-?\\d+(\\.\\d+)?");
    }
}
