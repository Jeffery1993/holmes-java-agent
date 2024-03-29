package com.jeffery.holmes.common.util;

import java.io.File;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

/**
 * LoggerFactory for common module and core module.
 */
public class LoggerFactory {

    private static FileHandler fileHandler;

    private static final int FILE_LIMIT = 3 * (1 << 20);
    private static final int FILE_COUNT = 3;

    static {
        try {
            File logDir = new File(PathManager.LOG_DIR_PATH);
            if (!logDir.exists()) {
                logDir.mkdirs();
            }
            fileHandler = new FileHandler(PathManager.LOG_FILE_PATH, FILE_LIMIT, FILE_COUNT, true);
            fileHandler.setLevel(Level.INFO);
            fileHandler.setFormatter(new SimpleFormatter());
        } catch (Exception e) {
            // ignore
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
        logger.setUseParentHandlers(false);
        return logger;
    }

}
