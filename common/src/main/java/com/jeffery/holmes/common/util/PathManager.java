package com.jeffery.holmes.common.util;

import java.io.File;

/**
 * Manager for paths.
 */
public class PathManager {

    /**
     * Base path for holmes agent.
     */
    public static final String BASE_PATH = System.getProperty("basePath");

    private static final String COMMON_JAR_FILE_NAME = "holmes-common.jar";
    public static final String COMMON_JAR_FILE_PATH = BASE_PATH + File.separator + COMMON_JAR_FILE_NAME;

    private static final String CONFIG_FILE_NAME = "holmes.config";
    public static final String CONFIG_FILE_PATH = BASE_PATH + File.separator + CONFIG_FILE_NAME;

    private static final String LOG_FILE_NAME = "holmes.log";
    private static final String LOG_BASE_PATH = System.getProperty("user.home") + File.separator + "holmes" + File.separator + "logs";
    public static final String LOG_DIR_PATH = LOG_BASE_PATH + File.separator + System.getProperty("clusterId") + " - " + System.getProperty("appId");
    public static final String LOG_FILE_PATH = LOG_DIR_PATH + File.separator + LOG_FILE_NAME;

}
