package drone;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class DroneTest {

    private final static int PORT = 10001;

    /**
     * Tests drone instantiation
     */
    @Test
    public void testDroneCreation() {
        Drone drone = new Drone();

        Assertions.assertNotNull(drone.getProximitySensor());
        Assertions.assertInstanceOf(ProximitySensor.class, drone.getProximitySensor());

        Assertions.assertNotNull(drone.getAccelerometerSensor());
        Assertions.assertInstanceOf(Accelerometer.class, drone.getAccelerometerSensor());

        Assertions.assertNotNull(drone.getCameraSensor());
        Assertions.assertInstanceOf(Camera.class, drone.getCameraSensor());
    }
}
