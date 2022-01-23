package it.unibo.dronesecurity.dronesystem.drone;

/**
 * Factory assisting in sensors' creation.
 */
public class SensorFactory {

    /**
     * Creates an accelerometer.
     *
     * @return The instantiated accelerometer
     */
    public Sensor getAccelerometer() {
        final Sensor accelerometer = new Accelerometer();
        accelerometer.activate();
        return accelerometer;
    }

    /**
     * Creates a proximity sensor.
     *
     * @return The instantiated proximity sensor
     */
    public Sensor getProximitySensor() {
        final Sensor proximitySensor = new ProximitySensor();
        proximitySensor.activate();
        return proximitySensor;
    }

    /**
     * Creates a camera.
     *
     * @return The instantiated camera
     */
    public Sensor getCamera() {
        final Sensor camera = new Camera();
        camera.activate();
        return camera;
    }
}
