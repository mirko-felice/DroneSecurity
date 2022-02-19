package it.unibo.dronesecurity.dronesystem.drone;


import java.util.Map;

/**
 * Item representing a drone with all its physical sensors.
 */
public class Drone {

    private static final SensorFactory SENSOR_FACTORY = new SensorFactory();
    private final transient Sensor<Double> proximity;
    private final transient Sensor<Map<String, Double>> accelerometer;
    private final transient Sensor<Double> camera;
    private transient boolean isMoving;

    /**
     * Constructs drone's sensors.
     */
    public Drone() {
        this.proximity = SENSOR_FACTORY.getProximitySensor();
        this.accelerometer = SENSOR_FACTORY.getAccelerometer();
        this.camera = SENSOR_FACTORY.getCamera();
    }

    /**
    * Executes the analysis of the raw data of all sensors.
    */
    public void readAllData() {
        this.proximity.readData();
        this.accelerometer.readData();
        this.camera.readData();
    }

    /**
     * Gets the proximity sensor data.
     *
     * @return The data read by the proximity sensor of the drone
     */
    public Double getProximitySensorData() {
        return this.proximity.getData();
    }

    /**
     * Gets the accelerometer data.
     *
     * @return The data read by the accelerometer of the drone
    */
    public Map<String, Double> getAccelerometerSensorData() {
        return this.accelerometer.getData();
    }

    /**
     * Gets the camera data.
     *
     * @return The data read by the camera of the drone
    */
    public Double getCameraSensorData() {
        return this.camera.getData();
    }

    /**
     * Activates the Drone, making it operative.
     */
    public void activate() {
        this.isMoving = true;
    }

    /**
     * Halts the Drone.
     */
    public void halt() {
        this.isMoving = false;
    }

    /**
     * Deactivates the Drone, making it inoperative.
     */
    public void deactivate() {
        this.isMoving = false;
        this.proximity.deactivate();
        this.accelerometer.deactivate();
        this.camera.deactivate();
    }

    /**
     * Checks if the Drone is operating (moving).
     * @return true if Drone is moving, false otherwise
     */
    public boolean isOperating() {
        return this.isMoving;
    }
}
