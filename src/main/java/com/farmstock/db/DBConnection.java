package com.farmstock.db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Objects;

public final class DBConnection {

    private static final String DEFAULT_URL = "jdbc:postgresql://<your-project>.supabase.co:5432/postgres?sslmode=require";
    private static final String DEFAULT_USER = "postgres";
    private static final String DEFAULT_PASSWORD = "<your-password>";

    private static final String ENV_URL = "SUPABASE_DB_URL";
    private static final String ENV_USER = "SUPABASE_DB_USER";
    private static final String ENV_PASSWORD = "SUPABASE_DB_PASSWORD";

    static {
        try {
            Class.forName("org.postgresql.Driver");
        } catch (ClassNotFoundException e) {
            throw new ExceptionInInitializerError("Failed to load PostgreSQL JDBC driver: " + e.getMessage());
        }
    }

    private DBConnection() {
    }

    public static Connection getConnection() throws SQLException {
        String url = getEnvOrDefault(ENV_URL, DEFAULT_URL);
        String user = getEnvOrDefault(ENV_USER, DEFAULT_USER);
        String password = getEnvOrDefault(ENV_PASSWORD, DEFAULT_PASSWORD);
        if (Objects.equals(url, DEFAULT_URL) || Objects.equals(user, DEFAULT_USER) || Objects.equals(password, DEFAULT_PASSWORD)) {
            throw new SQLException("Supabase credentials are not configured. Update DBConnection defaults or set environment variables SUPABASE_DB_URL, SUPABASE_DB_USER, and SUPABASE_DB_PASSWORD.");
        }
        return DriverManager.getConnection(url, user, password);
    }

    private static String getEnvOrDefault(String envKey, String defaultValue) {
        String value = System.getenv(envKey);
        return (value == null || value.isBlank()) ? defaultValue : value;
    }
}
