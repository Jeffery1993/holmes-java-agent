package com.jeffery.holmes.common.util;

import java.io.File;

public class PathManager {

    public static final String BASE_PATH = System.getProperty("basePath");

    private static final String CONFIG_FILE_NAME = "holmes.config";
    public static final String CONFIG_FILE_PATH = BASE_PATH + File.separator + CONFIG_FILE_NAME;

    private static final String LOG_FILE_NAME = "holmes.log";
    public static final String LOG_DIR_PATH = BASE_PATH + File.separator + "logs" + File.separator + System.getProperty("clusterId") + " - " + System.getProperty("appId");
    public static final String LOG_FILE_PATH = LOG_DIR_PATH + File.separator + LOG_FILE_NAME;

}
