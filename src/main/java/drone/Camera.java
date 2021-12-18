package drone;


import java.util.HashMap;
import java.util.Map;

/**
 * Item representing a real camera sensor and observing its values
 */
public class Camera extends AbstractSensor {

    private double data;

    @Override
    public void activate() {
        //TODO
        this.switchOn();
    }

    @Override
    public void readValue() {
        //TODO
    }

    @Override
    public double getReadableValue() {
        //TODO
        this.data = 3.0;
        return this.data;
    }
}
