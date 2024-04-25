package by.arvisit.cabapp.common.util;

import java.util.regex.Pattern;

public class ValidationRegexp {

    public static final String CARD_NUMBER = "[0-9]{16}";
    public static final String CAR_REGISTRATION_NUMBER_BY = "(E|[0-9])[0-9]{3}[ABEIKMHOPCTX]{2}-[1-7]";
    public static final String DATE_AS_FILTER_PARAM_VALIDATION_REGEXP = "[0-9]{4}(-(0[1-9]|1[0-2]))?(-(0[1-9]|1[0-2])-(0[1-9]|1[0-9]|2[0-9]|3[0-1]))?(-(0[1-9]|1[0-2])-(0[1-9]|1[0-9]|2[0-9]|3[0-1])T(0[1-9]|1[0-9]|2[0-3]))?";
    public static final Pattern DATE_AS_FILTER_PARAM_VALIDATION_COMPILED = Pattern.compile(DATE_AS_FILTER_PARAM_VALIDATION_REGEXP);

    private ValidationRegexp() {}

}
