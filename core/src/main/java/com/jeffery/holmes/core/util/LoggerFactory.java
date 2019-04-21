package com.jeffery.holmes.core.util;

import java.io.File;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

public class LoggerFactory {

    private static FileHandler fileHandler;

    private static final String USER_HOME = System.getProperty("user.home");
    private static final int FILE_LIMIT = 3 * (1 << 20);
    private static final int FILE_COUNT = 3;
    private static final String FILE_PATTERN = USER_HOME + File.separator + "holmes" + File.separator + "javaagent.log";

    static {
        try {
            fileHandler = new FileHandler(FILE_PATTERN, FILE_LIMIT, FILE_COUNT, true);
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
