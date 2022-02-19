package it.unibo.dronesecurity.userapplication.utilities;

import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

/**
 * Helper for Date stuff.
 */
public final class DateHelper {

    private static final String ORDER_DATE_FORMAT = "yyyy-MM-dd HH:mm:ss";
    /**
     * Date Formatter to parse and format date values.
     */
    public static final DateTimeFormatter FORMATTER =
            DateTimeFormatter.ofPattern(ORDER_DATE_FORMAT, Locale.ITALY).withZone(ZoneId.systemDefault());

    private DateHelper() { }
}
