package com.jeffery.holmes.core.util;

import com.jeffery.holmes.common.util.LoggerFactory;
import com.jeffery.holmes.common.util.PathManager;
import javassist.ClassClassPath;
import javassist.ClassPool;
import javassist.CtClass;
import javassist.NotFoundException;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.logging.Logger;

/**
 * Utility class for Javassist.
 */
public class JavassistUtils {

    private static final Logger LOGGER = LoggerFactory.getLogger(JavassistUtils.class);

    public static final ClassPool CLASS_POOL = ClassPool.getDefault();

    static {
        try {
            CLASS_POOL.insertClassPath(PathManager.COMMON_JAR_FILE_PATH);
        } catch (NotFoundException e) {
            LOGGER.severe("Failed to add " + PathManager.COMMON_JAR_FILE_PATH + " to classpath!");
        }
    }

    /**
     * Get {@link CtClass} from {@link ClassPool} according to className.
     *
     * @param className class name provided
     * @return {@link CtClass} associated with the class name provided
     * @throws NotFoundException
     */
    public static CtClass getCtClass(String className) throws NotFoundException {
        return CLASS_POOL.get(className);
    }

    /**
     * Get {@link CtClass} from {@link ClassPool} according to className, try to load the class with the given {@link ClassLoader} if not found.
     *
     * @param className class name provided
     * @param loader    classloader provided
     * @return {@link CtClass} associated with the class name provided
     * @throws NotFoundException
     */
    public static CtClass getCtClass(String className, ClassLoader loader) throws ClassNotFoundException, NotFoundException {
        try {
            return CLASS_POOL.get(className);
        } catch (NotFoundException e) {
            Class<?> clazz = loader.loadClass(className);
            CLASS_POOL.insertClassPath(new ClassClassPath(clazz));
            return CLASS_POOL.get(className);
        }
    }

    /**
     * Make {@link CtClass} from byte array.
     *
     * @param classfileBuffer classfile byte array provided
     * @return {@link CtClass} associated with the classfile byte array provided
     * @throws IOException
     */
    public static CtClass makeCtClass(byte[] classfileBuffer) throws IOException {
        InputStream inputStream = new ByteArrayInputStream(classfileBuffer);
        return CLASS_POOL.makeClass(inputStream);
    }

    /**
     * Build params as array of {@link CtClass} with the given classNames.
     *
     * @param classNames class names provided
     * @return array of {@link CtClass}(s) associated with the class names provided
     * @throws NotFoundException
     */
    public static CtClass[] buildParams(String... classNames) throws NotFoundException {
        CtClass[] params = new CtClass[classNames.length];
        for (int i = 0; i < classNames.length; i++) {
            params[i] = getCtClass(classNames[i]);
        }
        return params;
    }

}
