package com.jeffery.holmes.server;

import com.jeffery.holmes.server.index.IndexWriterFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class ServerApplication {

    private static final Logger LOGGER = LoggerFactory.getLogger(ServerApplication.class);

    public static void main(String[] args) {
        LOGGER.info("Holmes server is starting...");

        SpringApplication.run(ServerApplication.class, args);

        // add shutdown hook
        Runtime.getRuntime().addShutdownHook(new Thread(() -> stopServer()));
        LOGGER.info("Holmes server started!!!");
    }

    private static void stopServer() {
        IndexWriterFactory.close();
    }

}
