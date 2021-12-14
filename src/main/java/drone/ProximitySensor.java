package drone;


import java.util.Map;

/**
 * Item representing a real proximity sensor and observing its values
 */
public class ProximitySensor extends AbstractSensor {

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
    public Map<String, Double> getReadableValue() {
        //TODO
        return null;
    }
}
