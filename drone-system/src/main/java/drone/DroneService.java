package drone;

import software.amazon.awssdk.crt.io.ClientBootstrap;
import software.amazon.awssdk.crt.io.EventLoopGroup;
import software.amazon.awssdk.crt.io.HostResolver;
import software.amazon.awssdk.crt.mqtt.MqttClientConnection;
import software.amazon.awssdk.iot.AwsIotMqttConnectionBuilder;
import utilities.CustomLogger;

import java.nio.file.FileSystems;
import java.util.HashMap;
import java.util.Map;

/**
 * Service providing data about drone status and its sensors.
 */
public class DroneService {

    private static final int ANALIZER_SLEEP_DURATION = 2000;
    private static final int PORT = 10_001;
    private static final String SEP = FileSystems.getDefault().getSeparator();
    private static final String CERTIFICATE_FOLDER_PATH = "certs" + SEP;
    private static final String ENDPOINT = "a3mpt31aaosxce-ats.iot.us-west-2.amazonaws.com";
    private static final String CLIENT_ID = "Drone";
    private static final String CERTIFICATE_PATH = CERTIFICATE_FOLDER_PATH + "Drone.cert.pem";
    private static final String PRIVATE_KEY_PATH = CERTIFICATE_FOLDER_PATH + "Drone.private.key.pem";
    private static final int KEEP_ALIVE_SECONDS = 6;

    //Drone
    private final transient Drone drone;
    private transient Thread dataAnalyzer;
    private transient boolean active;

    // Connection
    private final transient Map<String, Double> sensorData;
    private final transient EventLoopGroup eventLoopGroup;
    private final transient MqttClientConnection connection;

    /**
     * Constructs the drone to be observed by this drone service.
     */
    public DroneService() {
        this.drone = new Drone();
        this.sensorData = new HashMap<>();
        this.initAnalyzer();
        this.active = false;

        this.eventLoopGroup = new EventLoopGroup(1);

        this.connection = AwsIotMqttConnectionBuilder
                .newMtlsBuilderFromPath(CERTIFICATE_PATH, PRIVATE_KEY_PATH)
                .withCertificateAuthorityFromPath("", CERTIFICATE_FOLDER_PATH + "root-CA.pem")
                .withBootstrap(new ClientBootstrap(this.eventLoopGroup, new HostResolver(this.eventLoopGroup)))
                .withClientId(CLIENT_ID)
                .withEndpoint(ENDPOINT)
                .withCleanSession(false)
                .withKeepAliveSecs(KEEP_ALIVE_SECONDS)
                .build();

        this.connection.connect();
    }

    /**
     * Activates the drone.
     */
    public void startDrone() {
        this.active = true;
        this.dataAnalyzer.start();
    }

    /**
     * Deactivates the drone.
     */
    public void stopDrone() {
        this.active = false;
        this.drone.getAccelerometerSensor().deactivate();
        this.drone.getCameraSensor().deactivate();
        this.drone.getProximitySensor().deactivate();
        this.eventLoopGroup.close();
        this.connection.disconnect();
        this.connection.close();
    }

    /**
     * Provides data useful to connect to the drone.
     *
     * @return the port on which the drone service is waiting for the connection
     */
    public int getConnectionData() {
        return PORT;
    }

    /**
     * Gives the last data read by all sensors.
     *
     * @return Last data read by all sensors
     */
    public Map<String, Double> getSensorData() {
        return this.sensorData;
    }

    private void initAnalyzer() {
        this.dataAnalyzer = new Thread(() -> {
            try {
                while (this.active) {
                    drone.analyzeData();
                    sensorData.put("accelerometer", drone.getAccelerometerSensor().getReadableValue());
                    sensorData.put("proximity", drone.getProximitySensor().getReadableValue());
                    sensorData.put("camera", drone.getCameraSensor().getReadableValue());

                    Thread.sleep(ANALIZER_SLEEP_DURATION);
                }
            } catch (InterruptedException e) {
                CustomLogger.getLogger(getClass().getName()).info(e.getMessage());
                //TODO
            }
        });
    }
}
