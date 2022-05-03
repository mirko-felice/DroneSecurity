package it.unibo.dronesecurity.userapplication.negligence.serializers;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import it.unibo.dronesecurity.lib.MqttMessageParameterConstants;
import it.unibo.dronesecurity.userapplication.auth.entities.Courier;
import it.unibo.dronesecurity.userapplication.auth.entities.Maintainer;
import it.unibo.dronesecurity.userapplication.negligence.NegligenceACL;
import it.unibo.dronesecurity.userapplication.negligence.entities.*;
import it.unibo.dronesecurity.userapplication.utilities.DateHelper;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.time.Instant;

/**
 * Deserialize {@link NegligenceReport} into {@link OpenNegligenceReport} or {@link ClosedNegligenceReport}.
 */
public final class NegligenceReportDeserializer extends JsonDeserializer<NegligenceReport> {

    private static final String ASSIGNER_NOT_EXPECTED = "Report assigner is not the real supervisor of %s as expected.";

    @Override
    public @Nullable NegligenceReport deserialize(@NotNull final JsonParser parser, final DeserializationContext ctx)
            throws IOException {
        try {
            final ObjectMapper mapper = (ObjectMapper) parser.getCodec();
            final ObjectNode root = mapper.readTree(parser);

            final String negligent = root.get(MqttMessageParameterConstants.NEGLIGENT_PARAMETER).asText();
            final Courier courier = NegligenceACL.retrieveCourier(negligent);

            final Maintainer maintainer = courier.getSupervisor();
            if (root.has("assigner") && !maintainer.getUsername().equals(root.get("assigner").asText()))
                throw new IllegalStateException(String.format(ASSIGNER_NOT_EXPECTED, courier.getUsername()));

            final JsonNode data = root.get("data");
            final double proximity = data.get(MqttMessageParameterConstants.PROXIMITY_PARAMETER).asDouble();
            final JsonNode accelerometerData = data.get(MqttMessageParameterConstants.ACCELEROMETER_PARAMETER);

            final BaseNegligenceReport.Builder builder = new BaseNegligenceReport.Builder(courier, maintainer)
                    .withProximity(proximity)
                    .withAccelerometerData(accelerometerData);

            final boolean isClosed = root.has("closingInstant");
            if (isClosed) {
                final Instant closingInstant = DateHelper.toInstant(root.get("closingInstant").asText());
                return new ClosedNegligenceReportImpl(builder, closingInstant);
            } else
                return new OpenNegligenceReportImpl(builder);
        } catch (InterruptedException e) {
            LoggerFactory.getLogger(this.getClass()).warn("Could not retrieve Courier from database", e);
            Thread.currentThread().interrupt();
            return null;
        }
    }

}
