package it.unibo.dronesecurity.lib;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Custom logger, decorator of Java {@link Logger}.
 */
public final class CustomLogger {

    private final transient Logger logger;
    private final transient boolean isDebug;

    private CustomLogger(final String name, final String resourceBundleName) {
        this.logger = Logger.getLogger(name, resourceBundleName);
        this.isDebug = System.getProperty("jdk.module.main") != null;
    }

    /**
     * Generate a new Logger related to the class name given.
     * NOTE: it will always generate a new java object, wrapping the same instance of {@link Logger} if already created.
     * @param name the class name related to
     * @return a new CustomLogger
     */
    @Contract("_ -> new")
    public static @NotNull CustomLogger getLogger(final String name) {
        return new CustomLogger(name, null);
    }

    /**
     * Shorthand for logging at INFO {@link Level}.
     * @param msg the message to log
     */
    public void info(final String msg) {
        this.log(Level.INFO, msg, null);
    }

    /**
     * Shorthand for logging at SEVERE {@link Level}.
     * @param msg the message to log
     * @param throwable the associated throwable
     */
    public void severe(final String msg, final Throwable throwable) {
        this.log(Level.SEVERE, msg, throwable);
    }

    private void log(final Level level, final String msg, final Throwable throwable) {
        if (this.isDebug) {
            final String message = this.logger.getName() + " -> \n" + msg + "\n";
            if (throwable == null)
                this.logger.log(level, message);
            else
                this.logger.log(level, message, throwable);
        }
    }
}
