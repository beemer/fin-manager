package com.finmanager;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.finmanager.db.DatabaseManager;
import com.finmanager.server.EmbeddedServer;

public class Main {
    private static final Logger logger = LoggerFactory.getLogger(Main.class);

    public static void main(String[] args) {
        logger.info("fin-manager starting...");
        
        // Initialize database
        DatabaseManager.getInstance();
        logger.info("Database initialized");
        
        // Start server
        EmbeddedServer server = new EmbeddedServer();
        try {
            server.start();
            logger.info("fin-manager API server running on http://localhost:8080");
            logger.info("Press Ctrl+C to stop");
        } catch (Exception e) {
            logger.error("Failed to start server: " + e.getMessage(), e);
        }
    }
}
