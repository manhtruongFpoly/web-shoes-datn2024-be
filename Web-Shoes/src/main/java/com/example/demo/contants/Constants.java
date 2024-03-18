package com.example.demo.contants;

/**
 * Application constants.
 */
public final class Constants {
    public static final char DEFAULT_ESCAPE_CHAR_QUERY = '\\';

    private Constants() {}

    public static final String DATE_FORMAT_YYYYMMDD = "yyyyMMdd";
    public static final Integer TIME_TYPE_DATE = 1;
    public static final Integer TIME_TYPE_MONTH = 2;
    public static final Integer TIME_TYPE_QUARTER = 3;
    public static final Integer TIME_TYPE_YEAR = 4;

    public static class RESPONSE_TYPE {
        public static final String SUCCESS = "SUCCESS";
        public static final String ERROR = "ERROR";
        public static final String WARNING = "WARNING";
        public static final String CONFIRM = "CONFIRM";
        public static final String invalidPermission = "invalidPermission";
    }

}
