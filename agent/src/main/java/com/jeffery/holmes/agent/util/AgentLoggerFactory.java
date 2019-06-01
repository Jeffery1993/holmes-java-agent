package com.jeffery.holmes.agent.util;

import java.io.File;
import java.lang.management.ManagementFactory;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

/**
 * LoggerFactory for agent module.
 */
public class AgentLoggerFactory {

    private static FileHandler fileHandler;

    private static final int FILE_LIMIT = 3 * (1 << 10);
    private static final int FILE_COUNT = 3;

    static {
        String codeSourceLocation = AgentLoggerFactory.class.getProtectionDomain().getCodeSource().getLocation().getPath();
        if (codeSourceLocation != null && codeSourceLocation.endsWith(".jar")) {
            if (codeSourceLocation.startsWith("/")) {
                codeSourceLocation = codeSourceLocation.substring(1);
            }
            String basePath = codeSourceLocation.substring(0, codeSourceLocation.lastIndexOf("/"));
            System.setProperty("basePath", basePath);
        }
        if (System.getProperty("clusterId") == null) {
            System.setProperty("clusterId", "unknown");
        }
        if (System.getProperty("appId") == null) {
            String name = ManagementFactory.getRuntimeMXBean().getName();
            String pid = name.split("@")[0];
            System.setProperty("appId", pid);
        }
    }

    private static final String LOG_FILE_NAME = "holmes-agent.log";
    private static final String LOG_BASE_PATH = System.getProperty("user.home") + File.separator + "holmes" + File.separator + "logs";
    private static final String LOG_DIR_PATH = LOG_BASE_PATH + File.separator + System.getProperty("clusterId") + " - " + System.getProperty("appId");
    private static final String LOG_FILE_PATH = LOG_DIR_PATH + File.separator + LOG_FILE_NAME;

    static {
        try {
            File logDir = new File(LOG_DIR_PATH);
            if (!logDir.exists()) {
                logDir.mkdirs();
            }
            fileHandler = new FileHandler(LOG_FILE_PATH, FILE_LIMIT, FILE_COUNT, true);
            fileHandler.setLevel(Level.INFO);
            fileHandler.setFormatter(new SimpleFormatter());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Return a logger named corresponding to the class passed as parameter.
     *
     * @param clazz class provided
     * @return logger associated with the class
     */
    public static Logger getLogger(Class<?> clazz) {
        Logger logger = Logger.getLogger(clazz.getName());
        if (fileHandler != null) {
            logger.addHandler(fileHandler);
        }
        return logger;
    }

}
