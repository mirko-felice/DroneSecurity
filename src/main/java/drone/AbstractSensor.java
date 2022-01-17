package drone;

/**
 * Abstract sensor that defines basic sensor behaviours.
 */
public abstract class AbstractSensor implements Sensor {

    private boolean on = false;
    private double lastReceivedValue;

    /**
     * Switches on this sensor.
     */
    protected void switchOn() {
        this.setOn(true);
    }

    /**
     * Switches off this sensor.
     */
    protected void switchOff() {
        this.setOn(false);
    }

    private void setOn(final boolean on) {
        this.on = on;
    }

    /**
     * Sets the value that the sensor had detected.
     *
     * @param value the value to memorize
     */
    protected void setLastReceivedValue(final double value) {
        this.lastReceivedValue = value;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public double getLastReceivedValue() {
        return this.lastReceivedValue;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isOn() {
        return this.on;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public abstract void activate();

    /**
     * {@inheritDoc}
     */
    @Override
    public abstract void readValue();

    /**
     * {@inheritDoc}
     */
    @Override
    public abstract double getReadableValue();
}
