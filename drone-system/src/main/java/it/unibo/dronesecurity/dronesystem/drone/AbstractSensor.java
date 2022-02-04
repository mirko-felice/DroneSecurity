package it.unibo.dronesecurity.dronesystem.drone;

import org.apache.commons.exec.*;
import it.unibo.dronesecurity.lib.CustomLogger;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.file.FileSystems;
import java.util.Locale;

/**
 * Abstract sensor that defines basic sensor behaviours.
 *
 * @param <R> type of sensor data
 */
public abstract class AbstractSensor<R> implements Sensor<R> {

    private static final int COMPATIBLE_PYTHON_MAJOR_VERSION = 3;
    private static final int COMPATIBLE_PYTHON_MINOR_VERSION = 7;
    private static final int SUCCESSFUL_TERMINATION_CODE = 1;
    private static final String SCRIPT_FOLDER = "scripts" + FileSystems.getDefault().getSeparator();
    private static final String CMD = System.getProperty("os.name").toLowerCase(Locale.getDefault()).contains("win")
                                                                                                        ? "python "
                                                                                                        : "python3 ";
    private final transient ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
    private final transient DefaultExecutor executor = new DefaultExecutor();

    private boolean on;

    private final transient Thread readingSensor = new Thread(() -> {
        if (this.isPythonVersionCompatible())
            this.executeScript(SCRIPT_FOLDER + this.getScriptName());
    });

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
     * {@inheritDoc}
     */
    @Override
    public abstract void readValue();

    /**
     * {@inheritDoc}
     */
    @Override
    public abstract R getReadableValue();

    /**
     * Initialize the executor for the sensor.
     */
    protected AbstractSensor() {
        final PumpStreamHandler streamHandler = new PumpStreamHandler(this.outputStream);
        this.executor.setStreamHandler(streamHandler);
        this.executor.setWatchdog(new ExecuteWatchdog(ExecuteWatchdog.INFINITE_TIMEOUT));
    }

    /**
     * Check the compatibility of python version with sensor scripts (should be 3.7 or greater).
     *
     * @return if installed python version is compatible or not
     */
    protected boolean isPythonVersionCompatible() {
        this.executeScript("--version");

        final String pythonVersion = this.outputStream.toString().trim().split(" ")[1];
        this.outputStream.reset();

        //Getting major version of python
        final int majorPythonVersion = Integer.parseInt(pythonVersion.split("\\.")[0]);
        //Getting minor version of python
        final int minorPythonVersion = Integer.parseInt(pythonVersion.split("\\.")[1]);

        return majorPythonVersion > COMPATIBLE_PYTHON_MAJOR_VERSION
                || majorPythonVersion == COMPATIBLE_PYTHON_MAJOR_VERSION
                && minorPythonVersion >= COMPATIBLE_PYTHON_MINOR_VERSION;
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
     * Checks if the device is a Raspberry.
     *
     * @return true if is Raspberry, false otherwise
     */
    protected boolean isRaspberry() {
        this.executeScript(SCRIPT_FOLDER + "os.py");

        final String nodeName = this.outputStream.toString().trim();
        this.outputStream.reset();

        return nodeName.toLowerCase(Locale.getDefault()).contains("raspberry");
    }

    /**
     * Gets the filename of the script that reads the sensor data.
     *
     * @return filename of the script
     */
    protected abstract String getScriptName();

    private void setOn(final boolean on) {
        this.on = on;
    }

    private void executeScript(final String pythonArgument) {
        final String versionControl = CMD + pythonArgument;
        final CommandLine cmdLine = CommandLine.parse(versionControl);

        try {
            this.executor.execute(cmdLine);
        } catch (IOException e) {
            CustomLogger.getLogger(getClass().getName()).info(e.getMessage());
        }
    }

}
