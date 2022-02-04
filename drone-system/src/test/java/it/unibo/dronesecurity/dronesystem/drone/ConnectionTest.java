package it.unibo.dronesecurity.dronesystem.drone;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import it.unibo.dronesecurity.lib.*;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import software.amazon.awssdk.crt.CrtRuntimeException;

import java.nio.charset.StandardCharsets;

/**
 * Testing the connection correctness.
 */
class ConnectionTest {

    private static final int DEACTIVATION_DELAY = 5000;

    /**
     * Tests drone connection with aws.
     */
    @Test
    void testConnection() {
        try {
            final DroneService service = new DroneService();

            Connection.getInstance();

            assertSensorDataRead();

            service.startDrone();

            Thread.sleep(DEACTIVATION_DELAY);
            service.stopDrone();
        } catch (InterruptedException | CrtRuntimeException e) {
            CustomLogger.getLogger(getClass().getName()).info(e.getMessage());
        }
    }

    private void assertSensorDataRead() {
        Connection.getInstance().subscribe(MqttTopicConstants.DATA_TOPIC, msg -> {
            final String jsonString = new String(msg.getPayload(), StandardCharsets.UTF_8);
            final JsonObject json = JsonParser.parseString(jsonString).getAsJsonObject();
            Assertions.assertTrue(json.has(MqttMessageParameterConstants.PROXIMITY_PARAMETER));
            Assertions.assertTrue(json.has(MqttMessageParameterConstants.ACCELEROMETER_PARAMETER));
            Assertions.assertTrue(json.has(MqttMessageParameterConstants.CAMERA_PARAMETER));
        });
    }
}
