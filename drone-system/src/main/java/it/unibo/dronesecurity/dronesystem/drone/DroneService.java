package it.unibo.dronesecurity.dronesystem.drone;

import com.google.gson.JsonObject;
import it.unibo.dronesecurity.dronesystem.utilities.CustomLogger;

import java.util.Map;

/**
 * Service providing data about drone status and its sensors.
 */
public class DroneService {

    private static final String TOPIC = "data";
    private static final int ANALIZER_SLEEP_DURATION = 500;
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
    public void startDrone() {
        this.active = true;
        this.dataAnalyzer.start();
        this.drone.start();
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

    private void initAnalyzer() {
        this.dataAnalyzer = new Thread(() -> {
            try {
                while (this.active) {
                    drone.analyzeData();

                    this.proximitySensorData = drone.getProximitySensor().getReadableValue();
                    this.accelerometerSensorData = drone.getAccelerometerSensor().getReadableValue();
                    this.cameraSensorData = drone.getCameraSensor().getReadableValue();
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

        Connection.getInstance().publish(TOPIC, mapJson);
    }
}
