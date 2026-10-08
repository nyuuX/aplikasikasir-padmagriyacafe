package com.padmagriya.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseUtil {
    private static final String DEFAULT_URL = "jdbc:mysql://localhost:3306/padmagriya_cafe?createDatabaseIfNotExist=true&useSSL=false&serverTimezone=Asia/Jakarta";
    private static final String DEFAULT_USER = "root";
    private static final String DEFAULT_PASSWORD = "";

    static {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException modernDriverMissing) {
            try {
                Class.forName("com.mysql.jdbc.Driver");
            } catch (ClassNotFoundException oldDriverMissing) {
                throw new ExceptionInInitializerError("MySQL Connector/J belum tersedia di library server.");
            }
        }
    }

    public static Connection getConnection() throws SQLException {
        String url = getSetting("DB_URL", DEFAULT_URL);
        String user = getSetting("DB_USER", DEFAULT_USER);
        String password = getSetting("DB_PASS", DEFAULT_PASSWORD);
        return DriverManager.getConnection(url, user, password);
    }

    private static String getSetting(String key, String defaultValue) {
        String value = System.getenv(key);
        if (value == null || value.trim().isEmpty()) {
            value = System.getProperty(key);
        }
        return value == null || value.trim().isEmpty() ? defaultValue : value;
    }
}
