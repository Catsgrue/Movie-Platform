package org.example.proiect_sgbd_vizionare_filme.config;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

import javax.sql.DataSource;
import java.io.InputStream;
import java.sql.Connection;
import java.util.Properties;

public class Database {

    private static final DataSource ds;

    static {
        Properties props = new Properties();

        try (InputStream input = Database.class.getClassLoader().getResourceAsStream("application.properties")) {
            if (input == null) {
                throw new RuntimeException("Error: Could not find the application.properties file in the resources.");
            }
            props.load(input);
        } catch (Exception e) {
            throw new RuntimeException("Critical error reading the database configuration.", e);
        }

        HikariConfig config = new HikariConfig();
        config.setJdbcUrl(props.getProperty("db.url"));
        config.setUsername(props.getProperty("db.username"));
        config.setPassword(props.getProperty("db.password"));

        config.setMaximumPoolSize(Integer.parseInt(props.getProperty("db.pool.size", "32")));

        ds = new HikariDataSource(config);
    }

    public static Connection getConnection() throws Exception {
        return ds.getConnection();
    }
}