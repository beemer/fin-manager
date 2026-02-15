package com.finmanager;

import com.finmanager.db.DatabaseManager;
import org.junit.BeforeClass;
import org.junit.AfterClass;

import java.lang.reflect.Field;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

public abstract class BaseTest {
    @BeforeClass
    public static void setupTestDatabase() throws Exception {
        // Use in-memory SQLite database for tests
        System.setProperty("db.url", "jdbc:sqlite::memory:");
        
        // Reset the DatabaseManager singleton
        Field instance = DatabaseManager.class.getDeclaredField("instance");
        instance.setAccessible(true);
        instance.set(null, null);
        
        // Initialize database
        DatabaseManager.getInstance();
    }

    @AfterClass
    public static void cleanupTestDatabase() throws Exception {
        // Clean up database connection
        try (Connection conn = DatabaseManager.getInstance().getConnection();
             Statement stmt = conn.createStatement()) {
            // Drop all tables
            stmt.execute("DROP TABLE IF EXISTS expenses");
            stmt.execute("DROP TABLE IF EXISTS recurring_expenses");
            stmt.execute("DROP TABLE IF EXISTS investments");
            stmt.execute("DROP TABLE IF EXISTS categories");
        } catch (SQLException e) {
            // Ignore errors during cleanup
        }
    }
}
