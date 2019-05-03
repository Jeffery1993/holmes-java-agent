package com.jeffery.holmes.core.util;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import java.util.logging.Logger;

public class ConfigManager {

    private static final Logger LOGGER = LoggerFactory.getLogger(ConfigManager.class);

    private static String clusterId;
    private static String appId;

    private static final Properties AGENT_PROPERTIES = new Properties();

    public static void init(String args) {
        try {
            InputStream inputStream = new FileInputStream(PathManager.CONFIG_FILE_PATH);
            AGENT_PROPERTIES.load(inputStream);
        } catch (IOException e) {
            LOGGER.severe("Failed to load config file: " + PathManager.CONFIG_FILE_PATH);
        }
    }

    public static String getProperty(String key) {
        return AGENT_PROPERTIES.getProperty(key);
    }

    public static String getClusterId() {
        return clusterId;
    }

    public static String getAppId() {
        return appId;
    }

}
