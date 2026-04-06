package com.stripe.utilities;

import java.sql.*;

/**
 * Manages JDBC database connections for test verification.
 *
 * Responsibilities:
 * - Open/close DB connections using config.properties
 * - Provide connection for query execution
 *
 * Usage:
 *   DatabaseManager db = new DatabaseManager();
 *   db.connect();
 *   ResultSet rs = db.executeQuery("SELECT ...");
 *   db.close();
 */
public class DatabaseManager {

    private Connection connection;

    public void connect() {
        try {
            String url = ConfigManager.getKey("db.url");
            String username = ConfigManager.getKey("db.username");
            String password = ConfigManager.getKey("db.password");

            connection = DriverManager.getConnection(url, username, password);
            System.out.println("DB Connection established successfully");
        } catch (SQLException e) {
            throw new RuntimeException("Failed to connect to database", e);
        }
    }

    public Connection getConnection() {
        return connection;
    }

    /**
     * Execute a SELECT query and return the ResultSet.
     */
    public ResultSet executeQuery(String query, Object... params) {
        try {
            PreparedStatement stmt = connection.prepareStatement(query);
            for (int i = 0; i < params.length; i++) {
                stmt.setObject(i + 1, params[i]);
            }
            return stmt.executeQuery();
        } catch (SQLException e) {
            throw new RuntimeException("Failed to execute query: " + query, e);
        }
    }

    /**
     * Execute an INSERT/UPDATE/DELETE and return affected row count.
     */
    public int executeUpdate(String query, Object... params) {
        try {
            PreparedStatement stmt = connection.prepareStatement(query);
            for (int i = 0; i < params.length; i++) {
                stmt.setObject(i + 1, params[i]);
            }
            return stmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Failed to execute update: " + query, e);
        }
    }

    public void close() {
        try {
            if (connection != null && !connection.isClosed()) {
                connection.close();
                System.out.println("DB Connection closed");
            }
        } catch (SQLException e) {
            throw new RuntimeException("Failed to close database connection", e);
        }
    }
}

