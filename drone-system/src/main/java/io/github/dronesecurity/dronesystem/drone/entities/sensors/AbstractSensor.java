/*
 * Copyright (c) 2021-2022, Mirko Felice & Maxim Derevyanchenko. All rights reserved.
 * Licensed under the MIT license. See LICENSE file in the project root for details.
 */

package io.github.dronesecurity.dronesystem.drone.entities.sensors;

import org.apache.commons.exec.CommandLine;
import org.apache.commons.exec.DefaultExecutor;
import org.apache.commons.exec.ExecuteWatchdog;
import org.apache.commons.exec.PumpStreamHandler;
import org.apache.commons.lang3.SystemUtils;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.slf4j.LoggerFactory;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.nio.file.attribute.FileAttribute;
import java.nio.file.attribute.PosixFilePermission;
import java.nio.file.attribute.PosixFilePermissions;
import java.util.Locale;
import java.util.Objects;
import java.util.Set;

/**
 * Abstract sensor that defines basic sensor behaviours.
 *
 * @param <SensorData> type of sensor data
 */
public abstract class AbstractSensor<SensorData> implements Sensor<SensorData> {

    private static final int COMPATIBLE_PYTHON_MAJOR_VERSION = 3;
    private static final int COMPATIBLE_PYTHON_MINOR_VERSION = 7;
    private static final int[] SUCCESSFUL_TERMINATION_CODES = {0, 1, 143};
    private static final String SCRIPT_EXTENSION = ".py";
    private static final String CMD = System.getProperty("os.name").toLowerCase(Locale.getDefault()).contains("win")
                                                                                                        ? "python "
                                                                                                        : "python3 ";
    private final ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
    private final DefaultExecutor executor = new DefaultExecutor();

    private boolean on;

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
        if (this.isPythonVersionCompatible()) {
            final String scriptFileName = this.getScriptFile(this.getScriptName());
            this.setOn(true);
            new Thread(() -> this.executeScript(scriptFileName)).start();
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void deactivate() {
        this.executor.setExitValues(SUCCESSFUL_TERMINATION_CODES);
        this.executor.getWatchdog().destroyProcess();
        try {
            this.outputStream.close();
        } catch (IOException e) {
            LoggerFactory.getLogger(getClass()).error("Failed to close output stream.", e);
        }
        this.setOn(false);
    }

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

        String pythonVersion;
        do {
            pythonVersion = this.outputStream.toString(StandardCharsets.UTF_8).trim();
        } while (pythonVersion.isEmpty());
        final String[] splitPythonVersion = pythonVersion.split(" ")[1].split("\\.");
        this.outputStream.reset();

        //Getting major version of python
        final int majorPythonVersion = Integer.parseInt(splitPythonVersion[0]);
        //Getting minor version of python
        final int minorPythonVersion = Integer.parseInt(splitPythonVersion[1]);

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
    protected final boolean isRaspberry() {
        this.executeScript(this.getScriptFile("os_detector"));

        String nodeName;
        do {
            nodeName = this.outputStream.toString(StandardCharsets.UTF_8).trim();
        } while (nodeName.isEmpty());
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
            LoggerFactory.getLogger(getClass()).error("Can NOT execute script (" + pythonArgument + ").", e);
        }
    }

    private @Nullable String getScriptFile(final @NotNull String scriptFileName) {
        try (InputStream is = Objects.requireNonNull(
                AbstractSensor.class.getResourceAsStream(scriptFileName + SCRIPT_EXTENSION))) {
            final Path path;
            if (SystemUtils.IS_OS_UNIX) {
                final FileAttribute<Set<PosixFilePermission>> attributes =
                        PosixFilePermissions.asFileAttribute(PosixFilePermissions.fromString("rwx------"));
                path = Files.createTempFile(scriptFileName, SCRIPT_EXTENSION, attributes);
            } else {
                path = Files.createTempFile(scriptFileName, SCRIPT_EXTENSION);
                final File file = path.toFile();
                final boolean isNotReadable = !file.setReadable(true, true);
                final boolean isNotWritable = !file.setWritable(true, true);
                final boolean isNotExecutable = !file.setExecutable(true, true);
                if (isNotReadable) {
                    LoggerFactory.getLogger(getClass()).error("Can NOT create readable file.");
                    return null;
                } else if (isNotWritable) {
                    LoggerFactory.getLogger(getClass()).error("Can NOT create writable file.");
                    return null;
                } else if (isNotExecutable) {
                    LoggerFactory.getLogger(getClass()).error("Can NOT create executable file.");
                    return null;
                }
            }
            Files.copy(is, path, StandardCopyOption.REPLACE_EXISTING);
            return path.toString();
        } catch (IOException e) {
            LoggerFactory.getLogger(getClass()).error("Can NOT read script file.", e);
        }
        return null;
    }
}
