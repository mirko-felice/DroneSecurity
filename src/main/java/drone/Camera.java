package drone;

/**
 * Item representing a real camera sensor and observing its values.
 */
public class Camera extends AbstractSensor {

    private static final double TEMPORAL_DATA = 3.0;
    private transient double data;

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
        this.data = TEMPORAL_DATA;
        return this.data;
    }
}
