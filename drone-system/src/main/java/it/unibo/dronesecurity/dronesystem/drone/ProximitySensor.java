package it.unibo.dronesecurity.dronesystem.drone;

/**
 * Item representing a real proximity sensor and observing its values.
 */
public class ProximitySensor extends AbstractSensor<Double> {

    private final transient String scriptFilename =  this.isRaspberry()
            ? "proximitySensor"
            : "proximitySimulator";
    private transient double distance;

    /**
     * {@inheritDoc}
     */
    @Override
    protected String getScriptName() {
        return this.scriptFilename;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void readValue() {
        if (getOutputStream().size() > 0) {
            final String[] values = getOutputStream().toString().trim().split("\n");
            this.distance = Double.parseDouble(values[values.length - 1]);
            getOutputStream().reset();
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Double getReadableValue() {
        return this.distance;
    }
}
