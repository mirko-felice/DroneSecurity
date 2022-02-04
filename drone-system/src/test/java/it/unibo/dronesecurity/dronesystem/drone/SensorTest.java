package it.unibo.dronesecurity.dronesystem.drone;

import it.unibo.dronesecurity.lib.CustomLogger;
import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

/**
 * Test for Sensors.
 */
class SensorTest {

    private static final int TERMINATION_DELAY = 500;
    private static final SensorFactory SENSOR_FACTORY = new SensorFactory();

    /**
     * Tests the instantiation of 3 available types of sensor.
     *
     * @param sensorType the type of the sensor to test
     */
    @ParameterizedTest
    @ValueSource(classes = {ProximitySensor.class, Accelerometer.class, Camera.class})
    void sensorCreationTest(final Class<Sensor<?>> sensorType) {
        final Sensor<?> sensor = initSensor(sensorType);
        Assertions.assertInstanceOf(sensorType, sensor);
        Assertions.assertTrue(sensor.isOn());
    }

    /**
     * Tests the deactivation of 3 available types of sensor.
     *
     * @param sensorType the type of the sensor to test
     */
    @ParameterizedTest
    @ValueSource(classes = {ProximitySensor.class, Accelerometer.class, Camera.class})
    void sensorDeactivationTest(final Class<Sensor<?>> sensorType) {
        final Sensor<?> sensor = initSensor(sensorType);
        Assertions.assertTrue(sensor.isOn());
        //Waiting scripts to terminate before deactivating the sensor
        try {
            Thread.sleep(TERMINATION_DELAY);
        } catch (InterruptedException e) {
            CustomLogger.getLogger(getClass().getName()).info(e.getMessage());
        }
        sensor.deactivate();
        Assertions.assertFalse(sensor.isOn());
    }

    /**
     * Instantiates a desired type of sensor.
     *
     * @param sensorClass type of the sensor to instantiate
     * @return the sensor instantiated
     */
    private Sensor<?> initSensor(final @NotNull Class<Sensor<?>> sensorClass) {
        if (sensorClass.equals(ProximitySensor.class))
            return SENSOR_FACTORY.getProximitySensor();
        else if (sensorClass.equals(Accelerometer.class))
            return SENSOR_FACTORY.getAccelerometer();
        else
            return SENSOR_FACTORY.getCamera();
    }
}
