package shipping.courier.serializers;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;

import java.io.IOException;
import java.io.Serial;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Deserialize Strings into Date objects.
 */
public final class DateDeserializer extends StdDeserializer<Date> {

    @Serial
    private static final long serialVersionUID = 4770114275875664008L;
    private static final String ORDER_DATE_FORMAT = "yyyy-MM-dd HH:mm:ss";

    /**
     * Build the Deserializer.
     */
    public DateDeserializer() {
        this(null);
    }

    /**
     * Build the Deserializer.
     * @param vc valueClass
     * @see StdDeserializer
     */
    public DateDeserializer(final Class<?> vc) {
        super(vc);
    }

    @Override
    public Date deserialize(final JsonParser parser, final DeserializationContext context) throws IOException {
        try {
            return new SimpleDateFormat(ORDER_DATE_FORMAT, Locale.ITALY).parse(parser.getText());
        } catch (ParseException e) {
            return null;
        }
    }
}
