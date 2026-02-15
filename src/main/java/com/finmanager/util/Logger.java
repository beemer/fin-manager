package com.finmanager.util;

import java.io.*;
import java.time.LocalDateTime;

public class Logger {
    private static Logger instance;
    private PrintWriter logWriter;
    private boolean consoleOutput = true;

    private Logger() {
        try {
            logWriter = new PrintWriter(new FileWriter("fin-manager.log", true), true);
        } catch (IOException e) {
            System.err.println("Failed to initialize logger");
            e.printStackTrace();
        }
    }

    public static Logger getInstance() {
        if (instance == null) {
            instance = new Logger();
        }
        return instance;
    }

    public void info(String message) {
        log("INFO", message);
    }

    public void warn(String message) {
        log("WARN", message);
    }

    public void error(String message) {
        log("ERROR", message);
    }

    public void error(String message, Exception e) {
        log("ERROR", message + " - " + e.getMessage());
        e.printStackTrace(logWriter);
    }

    public void debug(String message) {
        log("DEBUG", message);
    }

    private void log(String level, String message) {
        String timestamp = LocalDateTime.now().toString();
        String logMessage = String.format("[%s] %s - %s", timestamp, level, message);
        
        if (consoleOutput) {
            System.out.println(logMessage);
        }
        
        if (logWriter != null) {
            logWriter.println(logMessage);
        }
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
