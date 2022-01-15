package shipping.courier.serializers;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import utilities.DateHelper;

import java.io.IOException;
import java.text.ParseException;
import java.util.Date;

/**
 * Deserialize Strings into Date objects.
 */
public final class DateDeserializer extends JsonDeserializer<Date> {

    @Override
    public Date deserialize(final JsonParser parser, final DeserializationContext context) throws IOException {
        try {
            return DateHelper.FORMATTER.parse(parser.getText());
        } catch (ParseException e) {
            return null;
        }
    }
}
