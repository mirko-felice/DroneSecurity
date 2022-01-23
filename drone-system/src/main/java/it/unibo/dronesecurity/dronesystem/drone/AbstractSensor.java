package it.unibo.dronesecurity.dronesystem.drone;

import org.apache.commons.exec.*;
import it.unibo.dronesecurity.dronesystem.utilities.CustomLogger;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.file.FileSystems;
import java.util.Locale;

/**
 * Abstract sensor that defines basic sensor behaviours.
 */
public abstract class AbstractSensor implements Sensor {

    private static final int COMPATIBLE_PYTHON_MAJOR_VERSION = 3;
    private static final int COMPATIBLE_PYTHON_MINOR_VERSION = 7;
    private static final int SUCCESSFUL_TERMINATION_CODE = 1;
    protected static final String SCRIPT_FOLDER = "scripts" + FileSystems.getDefault().getSeparator();
    protected static final String CMD = System.getProperty("os.name").toLowerCase(Locale.getDefault()).contains("win")
                                                                                                        ? "python "
                                                                                                        : "python3 ";
    private final transient ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
    private final transient DefaultExecutor executor = new DefaultExecutor();

    private boolean on;
    private double lastReceivedValue;

    private final transient Thread readingSensor = new Thread(() -> {
        if (isPythonVersionCompatible())
            executeScript();
    });

    /**
     * Initialize the executor for the sensor.
     */
    protected AbstractSensor() {
        final PumpStreamHandler streamHandler = new PumpStreamHandler(this.outputStream);
        this.executor.setStreamHandler(streamHandler);
        this.executor.setWatchdog(new ExecuteWatchdog(ExecuteWatchdog.INFINITE_TIMEOUT));
    }

    private void setOn(final boolean on) {
        this.on = on;
    }

    /**
     * Check the compatibility of python version with sensor scripts (should be 3.7 or greater).
     *
     * @return if installed python version is compatible or not
     */
    protected boolean isPythonVersionCompatible() {
        final String versionControl = CMD + "--version";
        final CommandLine cmdLine = CommandLine.parse(versionControl);

        try {
            this.executor.execute(cmdLine);
        } catch (IOException e) {
            CustomLogger.getLogger(getClass().getName()).info(e.getMessage());
        }

        final String pythonVersion = this.outputStream.toString().trim().split(" ")[1];
        this.outputStream.reset();

        //Getting major version of python
        final int majorPythonVersion = Integer.parseInt(pythonVersion.split("\\.")[0]);
        //Getting minor version of python
        final int minorPythonVersion = Integer.parseInt(pythonVersion.split("\\.")[1]);

        return majorPythonVersion > COMPATIBLE_PYTHON_MAJOR_VERSION
               || majorPythonVersion == COMPATIBLE_PYTHON_MAJOR_VERSION
                  && minorPythonVersion > COMPATIBLE_PYTHON_MINOR_VERSION;
    }

    /**
     * Gives the output stream of the script execution.
     *
     * @return output stream of the script execution
     */
    protected ByteArrayOutputStream getOutputStream() {
        return this.outputStream;
    }

    /**
     * Gives the executor that will execute the script.
     *
     * @return the executor that executes the script
     */
    protected DefaultExecutor getExecutor() {
        return this.executor;
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
    public void activate() {
        this.readingSensor.start();
        this.setOn(true);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void deactivate() {
        this.executor.setExitValue(SUCCESSFUL_TERMINATION_CODE);
        this.executor.getWatchdog().destroyProcess();
        this.setOn(false);
    }

    /**
     * Code that executes the script that reads assigned sensor data.
     */
    protected abstract void executeScript();

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
