package drone;


import java.util.Map;

/**
 * Item representing a real camera sensor and observing its values
 */
public class Camera extends AbstractSensor {

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
