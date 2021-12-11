package drone;


import java.util.Map;

/**
* @generated
*/
public class Camera extends AbstractSensor {

    @Override
    public void activate() {
        this.switchOn();
    }

    @Override
    public void readValue() {

    }

    @Override
    public Map<String, Double> getReadableValue() {
        return null;
    }
}
