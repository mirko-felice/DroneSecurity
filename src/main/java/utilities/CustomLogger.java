package utilities;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Custom logger, decorator of Java {@link Logger}.
 */
public final class CustomLogger {

    private static final String PROPERTIES_FILENAME = "project.properties";
    private final transient Logger logger;
    private final transient boolean isDebug;

    private CustomLogger(final String name, final String resourceBundleName) {
        this.logger = Logger.getLogger(name, resourceBundleName);
        final Properties properties = new Properties();
        try (InputStream file = ClassLoader.getSystemResourceAsStream(PROPERTIES_FILENAME)) {
            properties.load(file);
        } catch (IOException e) {
            Logger.getGlobal().severe(e.getMessage());
        } finally {
            this.isDebug = Boolean.parseBoolean(properties.getProperty("isDebug"));
        }
    }

    /**
     * Generate a new Logger related to the class name given.
     * NOTE: it will always generate a new java object, wrapping the same instance of {@link Logger} if already created.
     * @param name the class name related to
     * @return a new CustomLogger
     */
    public static CustomLogger getLogger(final String name) {
        return new CustomLogger(name, null);
    }

    /**
     * Logs the message on specific level.
     * @param level the level to log on
     * @param msg the message to log
     */
    public void log(final Level level, final String msg) {
        if (this.isDebug)
            this.logger.log(level, msg);
    }

    /**
     * Shorthand for logging at INFO {@link Level}.
     * @param msg the message to log
     */
    public void info(final String msg) {
        log(Level.INFO, msg);
    }
}
