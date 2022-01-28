package it.unibo.dronesecurity.dronesystem.drone;

import java.util.Map;

/**
 * Factory assisting in sensors' creation.
 */
public class SensorFactory {

    /**
     * Creates an accelerometer.
     *
     * @return The instantiated accelerometer
     */
    public Sensor<Map<String, Double>> getAccelerometer() {
        final Sensor<Map<String, Double>> accelerometer = new Accelerometer();
        accelerometer.activate();
        return accelerometer;
    }

    /**
     * Creates a proximity sensor.
     *
     * @return The instantiated proximity sensor
     */
    public Sensor<Double> getProximitySensor() {
        final Sensor<Double> proximitySensor = new ProximitySensor();
        proximitySensor.activate();
        return proximitySensor;
    }

    /**
     * Creates a camera.
     *
     * @return The instantiated camera
     */
    public Sensor<Double> getCamera() {
        final Sensor<Double> camera = new Camera();
        camera.activate();
        return camera;
    }
}
