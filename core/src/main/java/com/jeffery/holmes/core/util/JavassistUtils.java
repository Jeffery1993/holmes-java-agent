package com.jeffery.holmes.core.util;

import javassist.ClassClassPath;
import javassist.ClassPool;
import javassist.CtClass;
import javassist.NotFoundException;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

public class JavassistUtils {

    public static final ClassPool CLASS_POOL = ClassPool.getDefault();

    public static void loadClass(ClassLoader loader, String className) throws ClassNotFoundException {
        Class<?> clazz = loader.loadClass(className);
        JavassistUtils.CLASS_POOL.insertClassPath(new ClassClassPath(clazz));
    }

    public static CtClass getCtClass(String className) throws NotFoundException {
        return CLASS_POOL.get(className);
    }

    public static CtClass makeCtClass(byte[] classfileBuffer) throws IOException {
        InputStream inputStream = new ByteArrayInputStream(classfileBuffer);
        return CLASS_POOL.makeClass(inputStream);
    }

    public static CtClass[] buildParams(String... classNames) throws NotFoundException {
        CtClass[] params = new CtClass[classNames.length];
        for (int i = 0; i < classNames.length; i++) {
            params[i] = getCtClass(classNames[i]);
        }
        return params;
    }

}
