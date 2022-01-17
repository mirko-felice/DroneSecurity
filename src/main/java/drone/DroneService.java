package drone;


import io.vertx.core.Vertx;
import io.vertx.core.http.HttpServer;
import io.vertx.core.http.ServerWebSocket;
import utilities.CustomLogger;

import java.util.HashMap;
import java.util.Map;

/**
 * Service providing data about drone status and its sensors.
 */
public class DroneService {

    private static final int ANALIZER_SLEEP_DURATION = 2000;
    private static final int PORT = 10_001;

    //Drone
    private final transient Drone drone;
    private transient Thread dataAnalyzer;
    private transient boolean active;

    // Connection
    private final transient Vertx vertx = Vertx.vertx();
    private transient ServerWebSocket socket;
    private final transient Map<String, Double> sensorData;

    /**
     * Constructs the drone to be observed by this drone service.
     */
    public DroneService() {
        this.drone = new Drone();
        this.sensorData = new HashMap<>();
        this.initAnalyzer();
        this.active = false;

        //Init server
        final HttpServer server = vertx.createHttpServer();
        server.webSocketHandler(socketConnection -> {
            socketConnection.accept();

            this.socket = socketConnection;
        });
        server.listen(PORT);
    }

    /**
     * Activates the drone.
     */
    public void startDrone() {
        this.active = true;
        this.dataAnalyzer.start();
    }

    /**
     * Provides data useful to connect to the drone.
     *
     * @return the port on which the drone service is waiting for the connection
     */
    public int getConnectionData() {
        return PORT;
    }

    private void initAnalyzer() {
        this.dataAnalyzer = new Thread(() -> {
            try {
                while (this.active) {
                    drone.analyzeData();
                    sensorData.put("accelerometer", drone.getAccelerometerSensor().getReadableValue());
                    sensorData.put("proximity", drone.getProximitySensor().getReadableValue());
                    sensorData.put("camera", drone.getCameraSensor().getReadableValue());
                    socket.writeTextMessage(sensorData.toString());

                    Thread.sleep(ANALIZER_SLEEP_DURATION);
                }
            } catch (InterruptedException e) {
                CustomLogger.getLogger(getClass().getName()).info(e.getMessage());
                //TODO
            }
        });
    }

}
