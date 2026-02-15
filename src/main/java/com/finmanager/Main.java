package com.finmanager;

import com.finmanager.db.DatabaseManager;
import com.finmanager.server.EmbeddedServer;

public class Main {
    public static void main(String[] args) {
        System.out.println("fin-manager starting...");
        
        // Initialize database
        DatabaseManager.getInstance();
        System.out.println("Database initialized");
        
        // Start server
        EmbeddedServer server = new EmbeddedServer();
        try {
            server.start();
            System.out.println("fin-manager API server running on http://localhost:8080");
            System.out.println("Press Ctrl+C to stop");
        } catch (Exception e) {
            System.err.println("Failed to start server: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
