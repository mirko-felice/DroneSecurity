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
import it.unibo.dronesecurity.userapplication.negligence.report.NegligenceReport;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Deserialize {@link NegligenceReport} using {@link NegligenceReport.Builder}.
 */
public final class NegligenceReportDeserializer extends JsonDeserializer<NegligenceReport> {

    @Override
    public @Nullable NegligenceReport deserialize(@NotNull final JsonParser parser, final DeserializationContext ctx)
            throws IOException {
        final ObjectMapper mapper = (ObjectMapper) parser.getCodec();
        final ObjectNode root = mapper.readTree(parser);

        final String negligent = root.get(MqttMessageParameterConstants.NEGLIGENT_PARAMETER).asText();
        final Courier courier;
        try {
            courier = NegligenceACL.retrieveCourier(negligent);

            final double proximity = root.get(MqttMessageParameterConstants.PROXIMITY_PARAMETER).asDouble();
            final JsonNode accelerometerData = root.get(MqttMessageParameterConstants.ACCELEROMETER_PARAMETER);
            final Map<String, Double> accelerometer = new ConcurrentHashMap<>();
            accelerometer.put(
                    MqttMessageParameterConstants.ACCELEROMETER_X_PARAMETER,
                    accelerometerData.get(MqttMessageParameterConstants.ACCELEROMETER_X_PARAMETER).asDouble());
            accelerometer.put(
                    MqttMessageParameterConstants.ACCELEROMETER_Y_PARAMETER,
                    accelerometerData.get(MqttMessageParameterConstants.ACCELEROMETER_Y_PARAMETER).asDouble());
            accelerometer.put(
                    MqttMessageParameterConstants.ACCELEROMETER_Z_PARAMETER,
                    accelerometerData.get(MqttMessageParameterConstants.ACCELEROMETER_Z_PARAMETER).asDouble());

            final Maintainer maintainer = courier.getSupervisor();

            return NegligenceReport.Builder.fromNegligent(courier, maintainer)
                    .withProximity(proximity)
                    .withAccelerometerData(accelerometer)
                    .build();
        } catch (InterruptedException e) {
            LoggerFactory.getLogger(this.getClass()).warn("Could not retrieve Courier from database", e);
            return null;
        }
    }

}
