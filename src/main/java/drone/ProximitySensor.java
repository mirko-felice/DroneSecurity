package drone;


import java.util.HashMap;
import java.util.Map;

/**
 * Item representing a real proximity sensor and observing its values
 */
public class ProximitySensor extends AbstractSensor {

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
        this.data = 2.0;
        return this.data;
    }
}
