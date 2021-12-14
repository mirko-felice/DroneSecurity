package drone;

import java.util.Map;

/**
 * Abstract sensor that defines basic sensor behaviours.
 */
public abstract class AbstractSensor implements Sensor {

    private boolean on = false;
    private double lastReceivedValue;

    /**
     * Provides publicly the information that the drone is active
     */
    protected void switchOn(){
        this.on = true;
    }

    /**
     * Provides publicly the information that the drone is NOT active
     */
    protected void switchOff() {
        this.on = false;
    }

    /**
     * Sets the value that the sensor had detected
     *
     * @param value the value to memorize
     */
    protected void setLastReceivedValue(double value) {
        this.lastReceivedValue = value;
    }

    @Override
    public double getValue() {
        return this.lastReceivedValue;
    }

    @Override
    public boolean isOn() {
        return this.on;
    }

    @Override
    public abstract void activate();

    @Override
    public abstract void readValue();

    @Override
    public abstract Map<String, Double> getReadableValue();
}
