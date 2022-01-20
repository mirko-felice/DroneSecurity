package utilities;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Locale;

/**
 * Helper for Date stuff.
 */
public final class DateHelper {

    private static final String ORDER_DATE_FORMAT = "yyyy-MM-dd HH:mm:ss";
    /**
     * Date Formatter to parse and format date values.
     */
    public static final DateFormat FORMATTER = new SimpleDateFormat(ORDER_DATE_FORMAT, Locale.ITALY);

    private DateHelper() { }
}
