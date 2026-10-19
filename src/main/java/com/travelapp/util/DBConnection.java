package com.travelapp.util;

import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Database utility for managing connections to MySQL (AWS EC2 / RDS / Local)
 * or SQLite (zero-config fallback).
 * 
 * Configurable via:
 * 1. Environment variables (DB_HOST, DB_PORT, DB_NAME, DB_USER, DB_PASSWORD, DB_TYPE)
 * 2. db.properties resource file
 */
public class DBConnection {
    private static final Logger LOGGER = Logger.getLogger(DBConnection.class.getName());
    private static final Properties props = new Properties();
    private static boolean tableInitialized = false;

    static {
        loadProperties();
    }

    private static void loadProperties() {
        try (InputStream in = DBConnection.class.getClassLoader().getResourceAsStream("db.properties")) {
            if (in != null) {
                props.load(in);
                LOGGER.info("db.properties successfully loaded from classpath.");
            } else {
                LOGGER.warning("db.properties not found in classpath, using default/env values.");
            }
        } catch (Exception e) {
            LOGGER.log(Level.WARNING, "Failed to read db.properties: " + e.getMessage(), e);
        }
    }

    private static String getParam(String envKey, String propKey, String defaultValue) {
        String envVal = System.getenv(envKey);
        if (envVal != null && !envVal.trim().isEmpty()) {
            return envVal.trim();
        }
        String sysVal = System.getProperty(propKey);
        if (sysVal != null && !sysVal.trim().isEmpty()) {
            return sysVal.trim();
        }
        return props.getProperty(propKey, defaultValue);
    }

    /**
     * Determines whether to use MySQL or SQLite.
     */
    public static String getDbType() {
        return getParam("DB_TYPE", "db.type", "mysql").toLowerCase();
    }

    /**
     * Retrieves a live JDBC Connection.
     */
    public static Connection getConnection() throws SQLException {
        String dbType = getDbType();
        Connection connection = null;

        if ("sqlite".equalsIgnoreCase(dbType)) {
            connection = getSqliteConnection();
        } else {
            try {
                connection = getMysqlConnection();
            } catch (SQLException e) {
                LOGGER.log(Level.WARNING, "Could not connect to MySQL (" + e.getMessage() + "). Checking fallback...");
                // If MySQL is specified but fails and user didn't explicitly disallow fallback,
                // rethrow so user knows to check their EC2 MySQL setup or connection string.
                throw e;
            }
        }

        if (!tableInitialized && connection != null) {
            initializeDatabaseTables(connection, dbType);
            tableInitialized = true;
        }

        return connection;
    }

    private static Connection getMysqlConnection() throws SQLException {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            throw new SQLException("MySQL JDBC Driver not found in classpath", e);
        }

        String host = getParam("DB_HOST", "db.host", "localhost");
        String port = getParam("DB_PORT", "db.port", "3306");
        String dbName = getParam("DB_NAME", "db.name", "travel_db");
        String user = getParam("DB_USER", "db.user", "traveluser");
        String password = getParam("DB_PASSWORD", "db.password", "");

        if ("root".equalsIgnoreCase(user)) {
            LOGGER.warning("SECURITY WARNING: Running as 'root' database user. Configure a dedicated 'traveluser' account for security.");
        }
        String useSSL = getParam("DB_USE_SSL", "db.useSSL", "false");
        String allowPublicKey = getParam("DB_ALLOW_PUB_KEY", "db.allowPublicKeyRetrieval", "true");
        String timezone = getParam("DB_SERVER_TIMEZONE", "db.serverTimezone", "UTC");

        String jdbcUrl = String.format(
            "jdbc:mysql://%s:%s/%s?useSSL=%s&allowPublicKeyRetrieval=%s&serverTimezone=%s",
            host, port, dbName, useSSL, allowPublicKey, timezone
        );

        LOGGER.info("Connecting to MySQL at " + host + ":" + port + "/" + dbName + " as " + user);
        return DriverManager.getConnection(jdbcUrl, user, password);
    }

    public static Connection getSqliteConnection() throws SQLException {
        try {
            Class.forName("org.sqlite.JDBC");
        } catch (ClassNotFoundException e) {
            throw new SQLException("SQLite JDBC Driver not found in classpath", e);
        }

        String filepath = getParam("SQLITE_FILEPATH", "sqlite.filepath", "travel_app.db");
        String jdbcUrl = "jdbc:sqlite:" + filepath;

        LOGGER.info("Connecting to SQLite database: " + jdbcUrl);
        return DriverManager.getConnection(jdbcUrl);
    }

    /**
     * Auto-creates the required `customer_data` table if it does not already exist.
     */
    private static synchronized void initializeDatabaseTables(Connection conn, String dbType) {
        String sql;
        if ("sqlite".equalsIgnoreCase(dbType)) {
            sql = "CREATE TABLE IF NOT EXISTS customer_data (" +
                  "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                  "name TEXT NOT NULL, " +
                  "age INTEGER NOT NULL, " +
                  "gender TEXT NOT NULL, " +
                  "bmi REAL NOT NULL, " +
                  "desired_country TEXT NOT NULL, " +
                  "created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP" +
                  ");";
        } else {
            sql = "CREATE TABLE IF NOT EXISTS customer_data (" +
                  "id INT AUTO_INCREMENT PRIMARY KEY, " +
                  "name VARCHAR(100) NOT NULL, " +
                  "age INT NOT NULL, " +
                  "gender VARCHAR(20) NOT NULL, " +
                  "bmi DECIMAL(5,2) NOT NULL, " +
                  "desired_country VARCHAR(100) NOT NULL, " +
                  "created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP" +
                  ") ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;";
        }

        try (Statement stmt = conn.createStatement()) {
            stmt.execute(sql);
            LOGGER.info("Verified/initialized 'customer_data' table in database.");
        } catch (SQLException e) {
            LOGGER.log(Level.WARNING, "Failed to auto-verify customer_data table: " + e.getMessage(), e);
        }
    }

    /**
     * Closes resources safely.
     */
    public static void close(AutoCloseable... closeables) {
        for (AutoCloseable c : closeables) {
            if (c != null) {
                try {
                    c.close();
                } catch (Exception ignored) {
                }
            }
        }
    }
}
