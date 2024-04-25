package by.arvisit.cabapp.common.util;

public final class SpecUtil {

    private SpecUtil() {
    }

    public static String toLikePattern(String str) {
        return "%" + str.toLowerCase() + "%";
    }

    public static Boolean fromString(String str) {
        if ("true".compareToIgnoreCase(str) == 0) {
            return true;
        } else if ("false".compareToIgnoreCase(str) == 0) {
            return false;
        } else {
            throw new IllegalArgumentException("Argument string could not be parsed as 'true' or 'false'");
        }
    }
}
