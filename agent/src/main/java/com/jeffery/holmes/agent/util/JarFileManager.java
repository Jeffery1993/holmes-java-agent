package com.jeffery.holmes.agent.util;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.jar.JarFile;

/**
 * Manager for jar files.
 */
public class JarFileManager {

    public static final String BASE_PATH = System.getProperty("basePath");

    private static final String COMMON_JAR_FILE_NAME = "holmes-common.jar";
    private static final String COMMON_JAR_FILE_PATH = BASE_PATH + File.separator + COMMON_JAR_FILE_NAME;

    private static final String CORE_JAR_FILE_NAME = "holmes-core.jar";
    private static final String CORE_JAR_FILE_PATH = BASE_PATH + File.separator + CORE_JAR_FILE_NAME;

    /**
     * Get common jar file, which is to be appended to bootstrap classpath.
     *
     * @return common jar file
     * @throws IOException
     */
    public static JarFile getCommonJarFile() throws IOException {
        return new JarFile(COMMON_JAR_FILE_PATH);
    }

    /**
     * Get core jar file.
     *
     * @return core jar file
     * @throws IOException
     */
    public static JarFile getCoreJarFile() throws IOException {
        return new JarFile(CORE_JAR_FILE_PATH);
    }

    /**
     * Get core jar file urls, which are used for classloader.
     *
     * @return core jar file urls
     * @throws IOException
     */
    public static URL[] getCoreJarFileURLs() throws IOException {
        File coreFile = new File(CORE_JAR_FILE_PATH);
        return new URL[]{coreFile.toURI().toURL()};
    }

}
