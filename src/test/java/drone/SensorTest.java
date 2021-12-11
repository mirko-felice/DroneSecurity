package drone;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

public class SensorTest {

    private static final SensorFactory SENSOR_FACTORY = new SensorFactory();

    @ParameterizedTest
    @ValueSource(classes = {Accelerometer.class, ProximitySensor.class, Camera.class})
    public void sensorCreationTest(Class<Sensor> sensorType) {
        Sensor sensor = initAccelerometer(sensorType);
        Assertions.assertInstanceOf(sensorType, sensor);
        Assertions.assertTrue(sensor.isOn());
    }

    private Sensor initAccelerometer(Class<Sensor> sensorClass) throws IllegalArgumentException{

        if (sensorClass.equals(Accelerometer.class))
            return SENSOR_FACTORY.getAccelerometer();
        else if (sensorClass.equals(ProximitySensor.class))
            return SENSOR_FACTORY.getProximitySensor();
        else if (sensorClass.equals(Camera.class))
            return SENSOR_FACTORY.getCamera();

        throw new IllegalArgumentException();
    }

}
