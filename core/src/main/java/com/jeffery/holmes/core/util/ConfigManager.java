package com.jeffery.holmes.core.util;

import java.io.File;
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
    private static final String CONFIG_FILE_NAME = "javaagent.config";
    private static final String CONFIG_FILE_PATH = PathManager.getRootPath() + File.separator + CONFIG_FILE_NAME;

    public static void init(String args) {
        try {
            InputStream inputStream = new FileInputStream(CONFIG_FILE_PATH);
            AGENT_PROPERTIES.load(inputStream);
        } catch (IOException e) {
            LOGGER.severe("Failed to load config file");
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
