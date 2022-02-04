package it.unibo.dronesecurity.lib;

import com.google.gson.JsonObject;
import org.jetbrains.annotations.NotNull;
import software.amazon.awssdk.crt.io.ClientBootstrap;
import software.amazon.awssdk.crt.io.EventLoopGroup;
import software.amazon.awssdk.crt.io.HostResolver;
import software.amazon.awssdk.crt.mqtt.MqttClientConnection;
import software.amazon.awssdk.crt.mqtt.MqttMessage;
import software.amazon.awssdk.crt.mqtt.QualityOfService;
import software.amazon.awssdk.iot.AwsIotMqttConnectionBuilder;

import java.nio.charset.StandardCharsets;
import java.nio.file.FileSystems;
import java.util.function.Consumer;

/**
 * Singleton representing connection with AWS.
 */
public final class Connection {

    private static final String SEP = FileSystems.getDefault().getSeparator();
    private static final String CERTIFICATE_FOLDER_PATH = ".." + SEP + "certs" + SEP;
    private static final String ENDPOINT = "a3mpt31aaosxce-ats.iot.us-west-2.amazonaws.com";
    private static final String CLIENT_ID = "Drone";
    private static final String CERTIFICATE_PATH = CERTIFICATE_FOLDER_PATH + "Drone.cert.pem";
    private static final String PRIVATE_KEY_PATH = CERTIFICATE_FOLDER_PATH + "Drone.private.key.pem";
    private static final int KEEP_ALIVE_SECONDS = 6;
    private static Connection singleton;
    private final transient MqttClientConnection clientConnection;
    private final transient EventLoopGroup eventLoopGroup;

    private Connection() {
        this.eventLoopGroup = new EventLoopGroup(1);
        this.clientConnection = AwsIotMqttConnectionBuilder
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
        this.clientConnection.connect();
    }

    /**
     * Gets the singleton for the connection.
     *
     * @return singleton for the connection
     */
    public static Connection getInstance() {
        synchronized (Connection.class) {
            if (singleton == null)
                singleton = new Connection();
            return singleton;
        }
    }

    /**
     * Publishes the message to the established connection.
     *
     * @param topic the topic to publish on
     * @param payload {@link JsonObject} to attach to the message
     */
    public void publish(final String topic, final @NotNull JsonObject payload) {
        this.clientConnection.publish(new MqttMessage(topic,
                payload.toString().getBytes(StandardCharsets.UTF_8),
                QualityOfService.AT_LEAST_ONCE));
    }

    /**
     * Subscribes to the topic to receive messages of the established connection.
     *
     * @param topic the topic to be subscribed to
     * @param consumer consumer that handles received messages
     */
    public void subscribe(final String topic, final Consumer<MqttMessage> consumer) {
        this.clientConnection.subscribe(topic, QualityOfService.AT_LEAST_ONCE, consumer);
    }

    /**
     * Closes established connection.
     */
    public void closeConnection() {
        this.eventLoopGroup.close();
        this.clientConnection.disconnect();
        this.clientConnection.close();
    }
}
