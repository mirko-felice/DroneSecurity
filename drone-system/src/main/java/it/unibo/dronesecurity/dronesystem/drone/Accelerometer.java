package it.unibo.dronesecurity.dronesystem.drone;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
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
            final JsonObject accelValues = JsonParser.parseString(jsonValues).getAsJsonObject()
                    .get(MqttMessageParameterConstants.ACCELEROMETER_PARAMETER).getAsJsonObject();
            accelValues.keySet().forEach(k -> this.values.put(k, accelValues.getAsJsonPrimitive(k).getAsDouble()));

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
