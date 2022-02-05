package it.unibo.dronesecurity.dronesystem.drone;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import it.unibo.dronesecurity.lib.CustomLogger;
import it.unibo.dronesecurity.lib.MqttMessageParameterConstants;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Item representing a real accelerometer sensor and observing its values.
 */
public class Accelerometer extends AbstractSensor<Map<String, Double>> {

    private final transient String scriptFilename =  this.isRaspberry()
            ? "accelerometer.py"
            : "accelerometerSimulator.py";
    private final transient Map<String, Double> values = new ConcurrentHashMap<>();

    /**
     * {@inheritDoc}
     */
    @Override
    protected String getScriptName() {
       return this.scriptFilename;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void readValue() {
        if (getOutputStream().size() > 0) {
            final String orig = getOutputStream().toString().trim();
            final int index = orig.lastIndexOf("\"accelerometer") - 1;

            final String jsonValues = orig.substring(index);

            try {
                final JsonNode accelValues = new ObjectMapper().readTree(jsonValues)
                        .get(MqttMessageParameterConstants.ACCELEROMETER_PARAMETER);
                accelValues.fields().forEachRemaining(k -> this.values.put(k.getKey(), k.getValue().asDouble()));
            } catch (JsonProcessingException e) {
                CustomLogger.getLogger(getClass().getName()).info(e.getMessage());
            }
            getOutputStream().reset();
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Map<String, Double> getReadableValue() {
        return this.values;
    }
}
