package com.jeffery.holmes.common.util;

import com.jeffery.holmes.common.consts.ConfigConsts;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import java.util.logging.Logger;

public class ConfigManager {

    private static final Logger LOGGER = LoggerFactory.getLogger(ConfigManager.class);

    private static String clusterId;
    private static String appId;
    private static String hostId;

    private static final Properties AGENT_PROPERTIES = new Properties();

    static {
        try {
            InputStream inputStream = new FileInputStream(PathManager.CONFIG_FILE_PATH);
            AGENT_PROPERTIES.load(inputStream);
        } catch (IOException e) {
            LOGGER.severe("Failed to load config file: " + e);
        }
        String ip = NetworkUtils.getIpAddress();
        if (ip != null) {
            System.setProperty(ConfigConsts.HOST_ID, ip);
        }
        clusterId = System.getProperty(ConfigConsts.CLUSTER_ID);
        appId = System.getProperty(ConfigConsts.APP_ID);
        hostId = System.getProperty(ConfigConsts.HOST_ID);
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

    public static String getHostId() {
        return hostId;
    }

    public static String[] getServerIps() {
        String serverIps = getProperty(ConfigConsts.SERVER_IPS);
        if (serverIps != null) {
            return serverIps.split(",");
        }
        return new String[0];
    }

    public static int getServerPort() {
        String serverPort = getProperty(ConfigConsts.SERVER_PORT);
        try {
            return Integer.parseInt(serverPort);
        } catch (Exception e) {
            return 0;
        }
    }

}
