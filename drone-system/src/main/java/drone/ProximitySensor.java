package drone;

import org.apache.commons.exec.CommandLine;
import utilities.CustomLogger;

import java.io.*;

/**
 * Item representing a real proximity sensor and observing its values.
 */
public class ProximitySensor extends AbstractSensor {

    private static final String SCRIPT_FILENAME = "test.py";
    private transient double distance;

    /**
     * {@inheritDoc}
     */
    @Override
    protected void executeScript() {
        try {
            final String proximitySensorScript = CMD + SCRIPT_FOLDER + SCRIPT_FILENAME;
            final CommandLine cmdLine = CommandLine.parse(proximitySensorScript);

            getExecutor().execute(cmdLine);
        } catch (IOException e) {
            CustomLogger.getLogger(getClass().getName()).info(e.getMessage());
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void readValue() {
        if (getOutputStream().size() > 0) {
            final String[] values = getOutputStream().toString().trim().split("\n");
            distance = Double.parseDouble(values[values.length - 1]);
            getOutputStream().reset();
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public double getReadableValue() {
        return distance;
    }
}
