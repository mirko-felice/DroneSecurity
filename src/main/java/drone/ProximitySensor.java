package drone;

/**
 * Item representing a real proximity sensor and observing its values.
 */
public class ProximitySensor extends AbstractSensor {

    private static final double TEMPORAL_DATA = 2.0;

    /**
     * {@inheritDoc}
     */
    @Override
    public void activate() {
        //TODO
        this.switchOn();

    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void readValue() {
        //TODO
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public double getReadableValue() {
        //TODO
        return TEMPORAL_DATA;
    }
}
