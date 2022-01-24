package it.unibo.dronesecurity.dronesystem.drone;

import com.google.gson.JsonObject;
import it.unibo.dronesecurity.dronesystem.utilities.CustomLogger;
import org.junit.jupiter.api.*;
import software.amazon.awssdk.crt.CrtRuntimeException;
import software.amazon.awssdk.crt.io.ClientBootstrap;
import software.amazon.awssdk.crt.io.EventLoopGroup;
import software.amazon.awssdk.crt.io.HostResolver;
import software.amazon.awssdk.crt.mqtt.MqttClientConnection;
import software.amazon.awssdk.crt.mqtt.QualityOfService;
import software.amazon.awssdk.iot.AwsIotMqttConnectionBuilder;

import java.nio.charset.StandardCharsets;
import java.nio.file.FileSystems;

/**
 * Testing the connection correctness.
 */
class ConnectionTest {

    private static final int DEACTIVATION_DELAY = 5000;

    private static final String TOPIC = "test/testing";
    private static final String SEP = FileSystems.getDefault().getSeparator();
    private static final String CERTIFICATE_FOLDER_PATH = ".." + SEP + "certs" + SEP;
    private static final String ENDPOINT = "a3mpt31aaosxce-ats.iot.us-west-2.amazonaws.com";
    private static final String CLIENT_ID = "Drone";
    private static final String CERTIFICATE_PATH = CERTIFICATE_FOLDER_PATH + "Drone.cert.pem";
    private static final String PRIVATE_KEY_PATH = CERTIFICATE_FOLDER_PATH + "Drone.private.key.pem";
    private static final int KEEP_ALIVE_SECONDS = 6;

    /**
     * Tests drone connection with aws.
     */
    @Test
    void testConnection() {
        DroneService service = null;
        try (
                EventLoopGroup eventLoopGroup =
                     new EventLoopGroup(1);

                MqttClientConnection connection =
                        AwsIotMqttConnectionBuilder
                                .newMtlsBuilderFromPath(CERTIFICATE_PATH, PRIVATE_KEY_PATH)
                                .withCertificateAuthorityFromPath("", CERTIFICATE_FOLDER_PATH + "root-CA.pem")
                                .withBootstrap(new ClientBootstrap(eventLoopGroup, new HostResolver(eventLoopGroup)))
                                .withClientId(CLIENT_ID)
                                .withEndpoint(ENDPOINT)
                                .withCleanSession(false)
                                .withKeepAliveSecs(KEEP_ALIVE_SECONDS)
                                .build()
        ) {
            service = new DroneService();
            connection.connect();

            assertSensorDataRead(connection);

            service.startDrone();

            Thread.sleep(DEACTIVATION_DELAY);
        } catch (InterruptedException e) {
            CustomLogger.getLogger(getClass().getName()).info(e.getMessage());
        } catch (CrtRuntimeException e) {
            CustomLogger.getLogger(getClass().getName()).info("No Certificate was found in the specified  folder.");
        } finally {
            if (service != null)
                service.stopDrone();
        }
    }

    private void assertSensorDataRead(final MqttClientConnection connection) {
        connection.subscribe(TOPIC, QualityOfService.EXACTLY_ONCE, msg -> {
            final String jsonString = new String(msg.getPayload(), StandardCharsets.UTF_8);
            final JsonObject json = new JsonObject().getAsJsonObject(jsonString);
            Assertions.assertTrue(json.has("accelerometer"));
            Assertions.assertTrue(json.has("proximity"));
            Assertions.assertTrue(json.has("camera"));
        });
    }
}
