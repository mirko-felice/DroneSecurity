package it.unibo.dronesecurity.userapplication.negligence.serializers;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import it.unibo.dronesecurity.userapplication.negligence.entities.DroneData;
import it.unibo.dronesecurity.userapplication.negligence.utilities.NegligenceConstants;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;

/**
 * Serialize {@link DroneData} into the corresponding Json.
 */
public class DroneDataSerializer extends JsonSerializer<DroneData> {

    /**
     * {@inheritDoc}
     */
    @Override
    public void serialize(final @NotNull DroneData value,
                          final @NotNull JsonGenerator gen,
                          final SerializerProvider serializers) throws IOException {
        gen.writeStartObject();
        gen.writeNumberField(NegligenceConstants.PROXIMITY, value.getProximity());
        gen.writeObjectField(NegligenceConstants.ACCELEROMETER, value.getAccelerometer());
        gen.writeEndObject();
        gen.flush();
    }
}
