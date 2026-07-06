package org.example.config;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

import java.sql.Connection;
import java.sql.SQLException;

public class ConnectionFactory {
    private static HikariDataSource dataSource;

    private static HikariDataSource createDataSource() {
        String url = System.getenv("url");
        String username = System.getenv("username");
        String password = System.getenv("password");

        if (url == null || username == null || password == null) {
            throw new IllegalStateException("Please define the url, username and password in the environment variables");
        }

        HikariConfig config = new HikariConfig();
        config.setJdbcUrl(url);
        config.setUsername(username);
        config.setPassword(password);
        config.setMaximumPoolSize(10);
        config.setMinimumIdle(2);
        config.setConnectionTimeout(30000);
        config.setIdleTimeout(600000);
        config.setMaxLifetime(1800000);

        return new HikariDataSource(config);
    }

    public static Connection getConnection() {
        if (dataSource == null) {
            dataSource = createDataSource();
        }
        try {
            return dataSource.getConnection();
        } catch (SQLException e) {
            throw new RuntimeException("Failed to get database connection", e);
        }
    }

    public static void shutdown() {
        if (dataSource != null && !dataSource.isClosed()) {
            dataSource.close();
        }
    }
}
