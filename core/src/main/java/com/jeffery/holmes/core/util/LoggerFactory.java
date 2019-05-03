package com.jeffery.holmes.core.util;

import java.io.File;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

public class LoggerFactory {

    private static FileHandler fileHandler;

    private static final int FILE_LIMIT = 3 * (1 << 20);
    private static final int FILE_COUNT = 3;

    static {
        File logDir = new File(PathManager.LOG_DIR_PATH);
        if (!logDir.exists()) {
            logDir.mkdirs();
        }
        try {
            fileHandler = new FileHandler(PathManager.LOG_FILE_PATH, FILE_LIMIT, FILE_COUNT, true);
        } catch (Exception e) {
            e.printStackTrace();
        }
        fileHandler.setLevel(Level.INFO);
        fileHandler.setFormatter(new SimpleFormatter());
    }

    public static Logger getLogger(Class<?> clazz) {
        Logger logger = Logger.getLogger(clazz.getName());
        logger.addHandler(fileHandler);
        logger.setUseParentHandlers(false);
        return logger;
    }

}
