package drone;

import java.util.Map;

public abstract class AbstractSensor implements Sensor {

    private boolean on = false;
    private double lastReceivedValue;

    protected void switchOn(){
        this.on = true;
    }

    protected void switchOff() {
        this.on = false;
    }

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
