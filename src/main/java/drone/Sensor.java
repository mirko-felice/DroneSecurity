package drone;

import java.util.Map;

public interface Sensor {
    double getValue();

    boolean isOn();

    void activate();

    void readValue();

    Map<String, Double> getReadableValue();
}
