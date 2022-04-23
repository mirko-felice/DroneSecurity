package it.unibo.dronesecurity.userapplication.negligence.serializers;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import it.unibo.dronesecurity.lib.MqttMessageParameterConstants;
import it.unibo.dronesecurity.userapplication.auth.entities.Courier;
import it.unibo.dronesecurity.userapplication.negligence.NegligenceACL;
import it.unibo.dronesecurity.userapplication.negligence.report.NegligenceReport;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Deserialize {@link NegligenceReport} using {@link NegligenceReport.Builder}.
 */
public final class NegligenceReportDeserializer extends JsonDeserializer<NegligenceReport> {

    @Override
    public @NotNull NegligenceReport deserialize(@NotNull final JsonParser parser, final DeserializationContext ctx)
            throws IOException {
        final ObjectMapper mapper = (ObjectMapper) parser.getCodec();
        final ObjectNode root = mapper.readTree(parser);

        final String negligent = root.get(MqttMessageParameterConstants.NEGLIGENT_PARAMETER).asText();
        final Courier courier = NegligenceACL.retrieveCourier(negligent);

        final double proximity = root.get(MqttMessageParameterConstants.PROXIMITY_PARAMETER).asDouble();
        final ConcurrentHashMap<String, Double> accelerometer = mapper.readValue(
                root.get(MqttMessageParameterConstants.ACCELEROMETER_PARAMETER).asText(),
                new AccelerometerTypeReference());

        return NegligenceReport.Builder.fromNegligent(courier)
                .withProximity(proximity)
                .withAccelerometerData(accelerometer)
                .build();
    }

    /**
     * {@link TypeReference} used to deserialize into Accelerometer data type.
     */
    private static class AccelerometerTypeReference extends TypeReference<ConcurrentHashMap<String, Double>> { }
}
