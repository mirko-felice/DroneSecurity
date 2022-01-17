package drone;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

/**
 * Test for Sensors.
 */
class SensorTest {

    private static final SensorFactory SENSOR_FACTORY = new SensorFactory();

    /**
     * Tests the instantiation of 3 available types of sensor.
     *
     * @param sensorType the type of the sensor to test
     */
    @ParameterizedTest
    @ValueSource(classes = {Accelerometer.class, ProximitySensor.class, Camera.class})
    void sensorCreationTest(final Class<Sensor> sensorType) {
        final Sensor sensor = initAccelerometer(sensorType);
        Assertions.assertInstanceOf(sensorType, sensor);
        Assertions.assertTrue(sensor.isOn());
    }

    /**
     * Instantiates a desired type of sensor.
     *
     * @param sensorClass type of the sensor to instantiate
     * @return the sensor instantiated
     */
    private Sensor initAccelerometer(final Class<Sensor> sensorClass) {
        if (sensorClass.equals(Accelerometer.class))
            return SENSOR_FACTORY.getAccelerometer();
        else if (sensorClass.equals(ProximitySensor.class))
            return SENSOR_FACTORY.getProximitySensor();
        else
            return SENSOR_FACTORY.getCamera();
    }
}
