package it.unibo.dronesecurity.dronesystem.drone;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import it.unibo.dronesecurity.dronesystem.utilities.CustomLogger;

import java.util.Map;
import java.util.Random;

/**
 * Service providing data about drone status and its sensors.
 */
public class DroneService {

    private static final String SYNC_TOPIC = "sync";
    private static final String DATA_TOPIC = "data";
    private static final String STATUS_PARAMETER = "status";
    private static final int TRAVELING_TIME = 5000;
    private static final int CLIENT_WAITING_TIME = 1000;
    private static final int ANALIZER_SLEEP_DURATION = 500;
    private static final int RANDOM_GENERATION_RANGE = 100;
    private static final int SUCCESS_PERCENTAGE = 70;
    private static final int PORT = 10_001;

    //Drone
    private final transient Drone drone;
    private final transient Analyzer analyzer;
    private transient Thread dataAnalyzer;
    private transient boolean active;

    // Connection
    private transient Double proximitySensorData;
    private transient Map<String, Double> accelerometerSensorData;
    private transient Double cameraSensorData;

    /**
     * Constructs the drone to be observed by this drone service.
     */
    public DroneService() {
        this.drone = new Drone();
        this.initAnalyzer();
        this.active = false;
        this.analyzer = new Analyzer();
    }

    /**
     * Activates the drone.
     */
    public void activateDrone() {
        this.active = true;
        Connection.getInstance().subscribe(SYNC_TOPIC, msg -> {
            final JsonObject json = JsonParser.parseString(new String(msg.getPayload())).getAsJsonObject();
            if ("start".equals(json.getAsJsonPrimitive("message").getAsString()))
                this.startDrone();
        });
    }

    /**
     * Deactivates the drone.
     */
    public void stopDrone() {
        this.active = false;
        this.drone.halt();
        this.drone.getAccelerometerSensor().deactivate();
        this.drone.getCameraSensor().deactivate();
        this.drone.getProximitySensor().deactivate();
        Connection.getInstance().closeConnection();
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
     * Gets the last read data from proximity sensor.
     *
     * @return last read data from proximity sensor
     */
    public Double getProximitySensorData() {
        return this.proximitySensorData;
    }

    /**
     * Gets the last read data from accelerometer sensor.
     *
     * @return last read data from accelerometer sensor
     */
    public Map<String, Double> getAccelerometerSensorData() {
        return this.accelerometerSensorData;
    }

    /**
     * Gets the last read data from camera.
     *
     * @return last read data from camera
     */
    public Double getCameraSensorData() {
        return this.cameraSensorData;
    }

    private void startDrone() {
        this.dataAnalyzer.start();
        this.drone.start();
        this.simulateDroneLifecycle();
    }

    //Simulates drone lifecycle sending its status to aws with 70% of successful delivers.
    private void simulateDroneLifecycle() {
        final JsonObject payload = new JsonObject();

        try {
            payload.addProperty(STATUS_PARAMETER, "delivering");
            Connection.getInstance().publish(SYNC_TOPIC, payload);

            Thread.sleep(TRAVELING_TIME);

            final int choice = new Random().nextInt(RANDOM_GENERATION_RANGE - 1);
            if (choice < SUCCESS_PERCENTAGE)
                payload.addProperty(STATUS_PARAMETER, "succeeded");
            else
                payload.addProperty(STATUS_PARAMETER, "failed");
            Thread.sleep(CLIENT_WAITING_TIME);
            Connection.getInstance().publish(SYNC_TOPIC, payload);
        } catch (InterruptedException e) {
            CustomLogger.getLogger(getClass().getName()).info(e.getMessage());
        }

        Connection.getInstance().subscribe(SYNC_TOPIC, msg -> {
            final JsonObject json = JsonParser.parseString(new String(msg.getPayload())).getAsJsonObject();
            if ("return".equals(json.getAsJsonPrimitive("message").getAsString())) {
                payload.addProperty(STATUS_PARAMETER, "returning");
                Connection.getInstance().publish(SYNC_TOPIC, payload);

                try {
                    Thread.sleep(TRAVELING_TIME);
                } catch (InterruptedException e) {
                    CustomLogger.getLogger(getClass().getName()).info(e.getMessage());
                }

                this.stopDrone();
            }
        });
    }

    private void initAnalyzer() {
        this.dataAnalyzer = new Thread(() -> {
            try {
                while (this.active) {
                    this.drone.analyzeData();

                    this.proximitySensorData = this.drone.getProximitySensor().getReadableValue();
                    this.accelerometerSensorData = this.drone.getAccelerometerSensor().getReadableValue();
                    this.cameraSensorData = this.drone.getCameraSensor().getReadableValue();
                    this.sendData();

                    this.analyzeData();
                    Thread.sleep(ANALIZER_SLEEP_DURATION);
                }
            } catch (InterruptedException e) {
                CustomLogger.getLogger(getClass().getName()).info(e.getMessage());
                //Restart Drone if thread interrupted
                this.startDrone();
            }
        });
    }

    private void analyzeData() {
        if (this.analyzer.isProximityCritical(this.proximitySensorData)
                || this.analyzer.isCriticalInclinationAngle(this.accelerometerSensorData))
            this.drone.halt();
    }

    private void sendData() {
        final JsonObject mapJson = new JsonObject();
        mapJson.addProperty("proximity", this.proximitySensorData);
        final JsonObject accelerometerValues = new JsonObject();
        this.accelerometerSensorData.forEach(accelerometerValues::addProperty);
        mapJson.add("accelerometer", accelerometerValues);
        mapJson.addProperty("camera", this.cameraSensorData);

        Connection.getInstance().publish(DATA_TOPIC, mapJson);
    }
}
