package com.finmanager.util;

import org.slf4j.LoggerFactory;

/**
 * Convenience wrapper for SLF4J logging.
 * Delegates to SLF4J for actual logging implementation.
 * 
 * Usage: Logger.info(ThisClass.class, "message");
 */
public class Logger {
    private Logger() {
        // Utility class
    }

    private static org.slf4j.Logger getLogger(Class<?> clazz) {
        return LoggerFactory.getLogger(clazz);
    }

    public static void info(Class<?> clazz, String message) {
        getLogger(clazz).info(message);
    }

    public static void warn(Class<?> clazz, String message) {
        getLogger(clazz).warn(message);
    }

    public static void error(Class<?> clazz, String message) {
        getLogger(clazz).error(message);
    }

    public static void error(Class<?> clazz, String message, Exception e) {
        getLogger(clazz).error(message, e);
    }

    public static void debug(Class<?> clazz, String message) {
        getLogger(clazz).debug(message);
    }

    public static void trace(Class<?> clazz, String message) {
        getLogger(clazz).trace(message);
    }
}

