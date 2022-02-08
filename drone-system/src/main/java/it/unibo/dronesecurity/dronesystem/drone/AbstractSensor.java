package it.unibo.dronesecurity.dronesystem.drone;

import it.unibo.dronesecurity.lib.CustomLogger;
import org.apache.commons.exec.CommandLine;
import org.apache.commons.exec.DefaultExecutor;
import org.apache.commons.exec.ExecuteWatchdog;
import org.apache.commons.exec.PumpStreamHandler;
import org.jetbrains.annotations.Nullable;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.Locale;
import java.util.Objects;

/**
 * Abstract sensor that defines basic sensor behaviours.
 *
 * @param <R> type of sensor data
 */
public abstract class AbstractSensor<R> implements Sensor<R> {

    private static final int COMPATIBLE_PYTHON_MAJOR_VERSION = 3;
    private static final int COMPATIBLE_PYTHON_MINOR_VERSION = 7;
    private static final int SUCCESSFUL_TERMINATION_CODE = 1;
    private static final String SCRIPT_EXTENSION = ".py";
    private static final String CMD = System.getProperty("os.name").toLowerCase(Locale.getDefault()).contains("win")
                                                                                                        ? "python "
                                                                                                        : "python3 ";
    private final transient ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
    private final transient DefaultExecutor executor = new DefaultExecutor();

    private boolean on;

    private final transient Thread readingSensor = new Thread(() -> {
        if (this.isPythonVersionCompatible())
            this.executeScript(this.getScriptFile(this.getScriptName()));
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
        this.executeScript(this.getScriptFile("os"));

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

    private @Nullable String getScriptFile(final String scriptFileName) {
        try (InputStream is =
                     Objects.requireNonNull(getClass().getResourceAsStream(scriptFileName + SCRIPT_EXTENSION))) {
            final Path path = Files.createTempFile(scriptFileName, SCRIPT_EXTENSION);
            Files.copy(is, path, StandardCopyOption.REPLACE_EXISTING);
            return path.toString();
        } catch (IOException e) {
            CustomLogger.getLogger(getClass().getName()).severe(e.getMessage(), e);
        }
        return null;
    }

}
