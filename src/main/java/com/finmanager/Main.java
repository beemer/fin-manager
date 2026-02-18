package com.finmanager;

import com.finmanager.util.Logger;
import com.finmanager.db.DatabaseManager;
import com.finmanager.server.EmbeddedServer;

public class Main {
    public static void main(String[] args) {
        Logger.info(Main.class, "fin-manager starting...");
        
        // Initialize database
        DatabaseManager.getInstance();
        Logger.info(Main.class, "Database initialized");
        
        // Start server
        EmbeddedServer server = new EmbeddedServer();
        try {
            server.start();
            Logger.info(Main.class, "fin-manager API server running on http://localhost:8080");
            Logger.info(Main.class, "Press Ctrl+C to stop");
        } catch (Exception e) {
            Logger.error(Main.class, "Failed to start server: " + e.getMessage(), e);
        }
    }
}
