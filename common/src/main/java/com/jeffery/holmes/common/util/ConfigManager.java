package com.jeffery.holmes.common.util;

import com.jeffery.holmes.common.consts.ConfigConsts;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import java.util.logging.Logger;

/**
 * Manager for configurations.
 */
public class ConfigManager {

    private static final Logger LOGGER = LoggerFactory.getLogger(ConfigManager.class);

    private static String clusterId;
    private static String appId;
    private static String hostId;

    private static final Properties AGENT_PROPERTIES = new Properties();
    private static final int DEFAULT_SERVER_PORT = 1024;

    static {
        InputStream inputStream = null;
        try {
            inputStream = new FileInputStream(PathManager.CONFIG_FILE_PATH);
            AGENT_PROPERTIES.load(inputStream);
        } catch (IOException e) {
            LOGGER.severe("Failed to load config file: " + e);
        } finally {
            IOUtils.closeQuietly(inputStream);
        }
        String ip = NetworkUtils.getIpAddress();
        if (ip != null) {
            System.setProperty(ConfigConsts.HOST_ID, ip);
        }
        clusterId = System.getProperty(ConfigConsts.CLUSTER_ID);
        appId = System.getProperty(ConfigConsts.APP_ID);
        hostId = System.getProperty(ConfigConsts.HOST_ID);
    }

    /**
     * Get property of agent by key.
     *
     * @param key key provided
     * @return value associated with the key
     */
    public static String getProperty(String key) {
        return AGENT_PROPERTIES.getProperty(key);
    }

    /**
     * Get the uuid for cluster.
     *
     * @return clusterId
     */
    public static String getClusterId() {
        return clusterId;
    }

    /**
     * Get the uuid for application.
     *
     * @return appId
     */
    public static String getAppId() {
        return appId;
    }

    /**
     * Get the uuid for host.
     *
     * @return hostId
     */
    public static String getHostId() {
        return hostId;
    }

    /**
     * Get server ips in array.
     *
     * @return server ips
     */
    public static String[] getServerIps() {
        String serverIps = getProperty(ConfigConsts.SERVER_IPS);
        if (serverIps != null) {
            return serverIps.split(",");
        }
        return new String[0];
    }

    /**
     * Get server port.
     *
     * @return server port
     */
    public static int getServerPort() {
        String serverPort = getProperty(ConfigConsts.SERVER_PORT);
        try {
            return Integer.parseInt(serverPort);
        } catch (Exception e) {
            return DEFAULT_SERVER_PORT;
        }
    }

}
