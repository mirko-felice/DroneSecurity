package it.unibo.dronesecurity.dronesystem.drone;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

/**
 * Test for Drone Service.
 */
class DroneTest {

    /**
     * Tests drone instantiation.
     */
    @Test
    void testDroneCreation() {
        final Drone drone = new Drone();

        Assertions.assertNotNull(drone.getProximitySensor());
        Assertions.assertInstanceOf(ProximitySensor.class, drone.getProximitySensor());

        Assertions.assertNotNull(drone.getAccelerometerSensor());
        Assertions.assertInstanceOf(Accelerometer.class, drone.getAccelerometerSensor());

        Assertions.assertNotNull(drone.getCameraSensor());
        Assertions.assertInstanceOf(Camera.class, drone.getCameraSensor());
    }
}
