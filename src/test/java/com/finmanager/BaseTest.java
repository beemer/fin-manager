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
        // Use H2 in-memory database for tests
        System.setProperty("db.url", "jdbc:h2:mem:testdb;DB_CLOSE_DELAY=-1;DB_CLOSE_ON_EXIT=FALSE");
        
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
            stmt.execute("DROP TABLE IF EXISTS investment_entries");
            stmt.execute("DROP TABLE IF EXISTS categories");
        } catch (SQLException e) {
            // Ignore errors during cleanup
        }
    }
}
