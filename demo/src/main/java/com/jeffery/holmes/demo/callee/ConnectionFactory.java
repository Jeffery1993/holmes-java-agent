package com.jeffery.holmes.demo.callee;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.sql.Connection;
import java.sql.DriverManager;

@Configuration
public class ConnectionFactory {

    private static final Logger LOGGER = LoggerFactory.getLogger(ConnectionFactory.class);

    @Value("${db.url}")
    private String url;
    @Value("${db.username}")
    private String user;
    @Value("${db.password}")
    private String password;

    static {
        try {
            Class.forName("com.mysql.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            LOGGER.error(e.getMessage(), e);
        }
    }

    @Bean
    public Connection getConnection() {
        try {
            return DriverManager.getConnection(url, user, password);
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
        }
        return null;
    }

}
