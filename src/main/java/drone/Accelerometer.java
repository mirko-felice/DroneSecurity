package drone;

import java.util.HashMap;
import java.util.Map;

/**
 * Item representing a real accelerometer sensor and observing its values
 */
public class Accelerometer extends AbstractSensor {

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
        this.data = 1.0;
        return this.data;
    }
}
