package com.finmanager.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Convenience wrapper for SLF4J logging.
 * Delegates to SLF4J for actual logging implementation.
 */
public class AppLogger {
    private static final Logger logger = LoggerFactory.getLogger(AppLogger.class);

    private AppLogger() {
        // Utility class
    }

    public static void info(String message) {
        logger.info(message);
    }

    public static void warn(String message) {
        logger.warn(message);
    }

    public static void error(String message) {
        logger.error(message);
    }

    public static void error(String message, Exception e) {
        logger.error(message, e);
    }

    public static void debug(String message) {
        logger.debug(message);
    }

    public static void trace(String message) {
        logger.trace(message);
    }

    public void setConsoleOutput(boolean enable) {
        this.consoleOutput = enable;
    }

    public void close() {
        if (logWriter != null) {
            logWriter.close();
        }
    }
}
