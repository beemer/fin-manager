package com.finmanager.db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;

public class DatabaseManager {
    private static final String DB_URL = System.getProperty("db.url", "jdbc:sqlite:fin-manager.db");
    private static DatabaseManager instance;

    private DatabaseManager() {
        initializeDatabase();
    }

    public static DatabaseManager getInstance() {
        if (instance == null) {
            instance = new DatabaseManager();
        }
        return instance;
    }

    public Connection getConnection() throws SQLException {
        return DriverManager.getConnection(DB_URL);
    }

    private void initializeDatabase() {
        try (Connection conn = getConnection(); Statement stmt = conn.createStatement()) {
            stmt.execute(
                """
                CREATE TABLE IF NOT EXISTS categories (
                  id INTEGER PRIMARY KEY AUTOINCREMENT,
                  name TEXT NOT NULL UNIQUE,
                  type TEXT NOT NULL,
                  color TEXT,
                  active BOOLEAN DEFAULT 1
                )
                """
            );

            stmt.execute(
                """
                CREATE TABLE IF NOT EXISTS expenses (
                  id INTEGER PRIMARY KEY AUTOINCREMENT,
                  date DATE NOT NULL,
                  amount REAL NOT NULL,
                  category_id INTEGER NOT NULL,
                  description TEXT,
                  recurring_id INTEGER,
                  is_recurring_instance BOOLEAN DEFAULT 0,
                  FOREIGN KEY(category_id) REFERENCES categories(id),
                  FOREIGN KEY(recurring_id) REFERENCES recurring_expenses(id)
                )
                """
            );

            stmt.execute(
                """
                CREATE TABLE IF NOT EXISTS recurring_expenses (
                  id INTEGER PRIMARY KEY AUTOINCREMENT,
                  category_id INTEGER NOT NULL,
                  amount REAL NOT NULL,
                  description TEXT,
                  frequency TEXT NOT NULL,
                  start_date DATE NOT NULL,
                  end_date DATE,
                  active BOOLEAN DEFAULT 1,
                  FOREIGN KEY(category_id) REFERENCES categories(id)
                )
                """
            );

            stmt.execute(
                """
                CREATE TABLE IF NOT EXISTS investment_entries (
                  id INTEGER PRIMARY KEY AUTOINCREMENT,
                  date DATE NOT NULL,
                  amount REAL NOT NULL,
                  currency TEXT DEFAULT 'USD',
                  exchange_rate REAL DEFAULT 1.0,
                  description TEXT,
                  is_recurring BOOLEAN DEFAULT 0
                )
                """
            );

            stmt.execute("CREATE INDEX IF NOT EXISTS idx_expenses_date ON expenses(date)");
            stmt.execute("CREATE INDEX IF NOT EXISTS idx_expenses_category ON expenses(category_id)");
            stmt.execute("CREATE INDEX IF NOT EXISTS idx_recurring_category ON recurring_expenses(category_id)");
            stmt.execute("CREATE INDEX IF NOT EXISTS idx_investments_date ON investment_entries(date)");

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void cleanupOldData() {
        LocalDate threeYearsAgo = LocalDate.now().minusYears(3);
        String sql = "DELETE FROM expenses WHERE date < ?";
        
        try (Connection conn = getConnection(); 
             var pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, threeYearsAgo.toString());
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
