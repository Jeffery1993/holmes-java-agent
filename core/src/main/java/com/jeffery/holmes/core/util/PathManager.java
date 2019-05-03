package com.jeffery.holmes.core.util;

import com.jeffery.holmes.premain.util.JarFileManager;

import java.io.File;

public class PathManager extends JarFileManager {

    private static final String CONFIG_FILE_NAME = "javaagent.config";
    public static final String CONFIG_FILE_PATH = getBasePath() + File.separator + CONFIG_FILE_NAME;

    private static final String LOG_FILE_NAME = "javaagent.log";
    public static final String LOG_DIR_PATH = getBasePath() + File.separator + "logs";
    public static final String LOG_FILE_PATH = LOG_DIR_PATH + File.separator + LOG_FILE_NAME;

}
