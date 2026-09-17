package org.example.proiect_sgbd_vizionare_filme.database;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

import javax.sql.DataSource;
import java.sql.Connection;

public class Database {

    private static final DataSource ds;

    static{
        HikariConfig config=new HikariConfig();
        config.setJdbcUrl("jdbc:oracle:thin:@localhost:1521:xe");
        config.setUsername("student");
        config.setPassword("123456");
        config.setMaximumPoolSize(32);

        ds=new HikariDataSource(config);
    }

    public static Connection getConnection() throws Exception{
        return ds.getConnection();
    }

}
