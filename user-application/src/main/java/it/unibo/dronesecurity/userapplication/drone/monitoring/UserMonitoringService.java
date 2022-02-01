package it.unibo.dronesecurity.userapplication.drone.monitoring;

import io.vertx.core.json.JsonObject;
import it.unibo.dronesecurity.userapplication.events.CriticalEvent;
import it.unibo.dronesecurity.userapplication.events.DataReadEvent;
import it.unibo.dronesecurity.userapplication.events.DomainEvents;
import it.unibo.dronesecurity.userapplication.events.WarningEvent;
import software.amazon.awssdk.crt.io.ClientBootstrap;
import software.amazon.awssdk.crt.io.EventLoopGroup;
import software.amazon.awssdk.crt.io.HostResolver;
import software.amazon.awssdk.crt.mqtt.MqttClientConnection;
import software.amazon.awssdk.crt.mqtt.QualityOfService;
import software.amazon.awssdk.iot.AwsIotMqttConnectionBuilder;

import java.nio.file.FileSystems;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Service that monitors drone data channels.
 */
public final class UserMonitoringService {

    private static final String READING_TOPIC = "data";
    private static final String WARNING_TOPIC = "warning";
    private static final String CRITICAL_TOPIC = "critical";
    private static final String SEP = FileSystems.getDefault().getSeparator();
    private static final String CERTIFICATE_FOLDER_PATH = ".." + SEP + "certs" + SEP;
    private static final String ENDPOINT = "a3mpt31aaosxce-ats.iot.us-west-2.amazonaws.com";
    private static final String CLIENT_ID = "User";
    private static final String CERTIFICATE_PATH = CERTIFICATE_FOLDER_PATH + "Drone.cert.pem";
    private static final String PRIVATE_KEY_PATH = CERTIFICATE_FOLDER_PATH + "Drone.private.key.pem";
    private static final int KEEP_ALIVE_SECONDS = 6;

    private final transient EventLoopGroup eventLoopGroup;
    private final transient MqttClientConnection connection;

    /**
     * Creates the connection with AWS service.
     */
    public UserMonitoringService() {
        this.eventLoopGroup = new EventLoopGroup(1);
        this.connection = AwsIotMqttConnectionBuilder
                .newMtlsBuilderFromPath(CERTIFICATE_PATH, PRIVATE_KEY_PATH)
                .withCertificateAuthorityFromPath(
                        System.getProperty("os.name").contains("win") ? "" : CERTIFICATE_FOLDER_PATH,
                        CERTIFICATE_FOLDER_PATH + "root-CA.pem")
                .withBootstrap(new ClientBootstrap(this.eventLoopGroup, new HostResolver(this.eventLoopGroup)))
                .withClientId(CLIENT_ID)
                .withEndpoint(ENDPOINT)
                .withCleanSession(false)
                .withKeepAliveSecs(KEEP_ALIVE_SECONDS)
                .build();
        this.connection.connect();
    }

    /**
     * Subscribes to the reading topic.
     *
     * @param domainEvents Domain to raise events on
     */
    public void subscribeToDataReading(final DomainEvents<DataReadEvent> domainEvents) {
        this.connection.subscribe(READING_TOPIC, QualityOfService.AT_LEAST_ONCE, msg -> {
            final JsonObject json = new JsonObject(new String(msg.getPayload()));
            final double proximity = json.getDouble("proximity");

            final JsonObject accelerometerJson = json.getJsonObject("accelerometer");
            final Map<String, Double> accelerometer = new ConcurrentHashMap<>();
            accelerometer.put("x", accelerometerJson.getDouble("x"));
            accelerometer.put("y", accelerometerJson.getDouble("y"));
            accelerometer.put("z", accelerometerJson.getDouble("z"));

            final double camera = json.getDouble("camera");

            domainEvents.raise(new DataReadEvent(proximity, accelerometer, camera));
        });
    }

    /**
     * Subscribes to the warning topic.
     *
     * @param domainEvents Domain to raise events on
     */
    public void subscribeToWarning(final DomainEvents<WarningEvent> domainEvents) {
        this.connection.subscribe(WARNING_TOPIC, QualityOfService.AT_LEAST_ONCE, msg -> {
            final JsonObject json = new JsonObject(new String(msg.getPayload()));
            domainEvents.raise(new WarningEvent(json.getString("message")));
        });
    }

    /**
     * Subscribes to the critical topic.
     *
     * @param domainEvents Domain to raise events on
     */
    public void subscribeToCritical(final DomainEvents<CriticalEvent> domainEvents) {
        this.connection.subscribe(CRITICAL_TOPIC, QualityOfService.AT_LEAST_ONCE, msg -> {
            final JsonObject json = new JsonObject(new String(msg.getPayload()));
            domainEvents.raise(new CriticalEvent(json.getString("message")));
        });
    }

    /**
     * Closes the active connection with AWS service.
     */
    public void closeConnection() {
        this.eventLoopGroup.close();
        this.connection.disconnect();
        this.connection.close();
    }
}
