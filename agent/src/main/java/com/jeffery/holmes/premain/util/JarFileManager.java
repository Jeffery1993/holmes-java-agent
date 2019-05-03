package com.jeffery.holmes.premain.util;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.jar.JarFile;

public class JarFileManager {

    private static String basePath;
    private static final String COMMON_JAR_FILE_NAME = "holmes-common.jar";
    private static final String CORE_JAR_FILE_NAME = "holmes-core.jar";

    static {
        String codeSourceLocation = JarFileManager.class.getProtectionDomain().getCodeSource().getLocation().getPath();
        if (codeSourceLocation != null && codeSourceLocation.endsWith(".jar")) {
            if (codeSourceLocation.startsWith("/")) {
                codeSourceLocation = codeSourceLocation.substring(1);
            }
            basePath = codeSourceLocation.substring(0, codeSourceLocation.lastIndexOf("/"));
        }
    }

    public static String getBasePath() {
        return basePath;
    }

    public static JarFile getCommonJarFile() throws IOException {
        return new JarFile(basePath + File.separator + COMMON_JAR_FILE_NAME);
    }

    public static JarFile getCoreJarFile() throws IOException {
        return new JarFile(basePath + File.separator + CORE_JAR_FILE_NAME);
    }

    public static URL[] getCoreJarFileURLs() throws IOException {
        File coreFile = new File(basePath + File.separator + CORE_JAR_FILE_NAME);
        return new URL[]{coreFile.toURI().toURL()};
    }

}
