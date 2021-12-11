package drone;

public class SensorFactory {

    public Sensor getAccelerometer(){
        Sensor accelerometer = new Accelerometer();
        accelerometer.activate();
        return accelerometer;
    }

    public Sensor getProximitySensor() {
        Sensor proximitySensor = new ProximitySensor();
        proximitySensor.activate();
        return proximitySensor;
    }

    public Sensor getCamera() {
        Sensor camera = new Camera();
        camera.activate();
        return camera;
    }
}
