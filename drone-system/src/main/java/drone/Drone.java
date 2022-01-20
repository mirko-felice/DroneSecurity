package drone;


/**
 * Item representing a drone with all its physical sensors.
 */
public class Drone {

    private static final SensorFactory SENSOR_FACTORY = new SensorFactory();
    private final transient Sensor proximity;
    private final transient Sensor accelerometer;
    private final transient Sensor camera;

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
    public void analyzeData() {
        this.proximity.readValue();
        this.accelerometer.readValue();
        this.camera.readValue();
    }

    /**
     * Gets the proximity sensor.
     *
     * @return The proximity sensor of the drone
     */
    public Sensor getProximitySensor() {
        return this.proximity;
    }

    /**
     * Gets the accelerometer.
     *
     * @return The accelerometer of the drone
    */
    public Sensor getAccelerometerSensor() {
        return this.accelerometer;
    }

    /**
     * Gets the camera.
     *
     * @return The camera of the drone
    */
    public Sensor getCameraSensor() {
        return this.camera;
    }

}
