package by.arvisit.cabapp.common.util;

public final class SpecUtil {

    private SpecUtil() {
    }

    public static String toLikePattern(String str) {
        return "%" + str.toLowerCase() + "%";
    }
}
