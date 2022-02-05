package it.unibo.dronesecurity.dronesystem.drone;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import it.unibo.dronesecurity.lib.Connection;
import it.unibo.dronesecurity.lib.CustomLogger;
import it.unibo.dronesecurity.lib.MqttMessageParameterConstants;
import it.unibo.dronesecurity.lib.MqttTopicConstants;
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

            Assertions.assertNotNull(Connection.getInstance());
            this.assertSensorDataRead();

            service.activateDrone();

            Thread.sleep(DEACTIVATION_DELAY);
            service.stopDrone();
        } catch (InterruptedException | CrtRuntimeException e) {
            CustomLogger.getLogger(getClass().getName()).info(e.getMessage());
        }
    }

    private void assertSensorDataRead() {
        Connection.getInstance().subscribe(MqttTopicConstants.DATA_TOPIC, msg -> {
            try {
                final String jsonString = new String(msg.getPayload(), StandardCharsets.UTF_8);
                final JsonNode json = new ObjectMapper().readTree(jsonString);
                Assertions.assertTrue(json.has(MqttMessageParameterConstants.PROXIMITY_PARAMETER));
                Assertions.assertTrue(json.has(MqttMessageParameterConstants.ACCELEROMETER_PARAMETER));
                Assertions.assertTrue(json.has(MqttMessageParameterConstants.CAMERA_PARAMETER));
            } catch (JsonProcessingException e) {
                CustomLogger.getLogger(getClass().getName()).info(e.getMessage());
            }
        });
    }
}
