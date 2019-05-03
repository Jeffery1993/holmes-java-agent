package com.jeffery.holmes.core.util;

import javassist.ClassPool;
import javassist.CtClass;
import javassist.NotFoundException;

public class JavassistUtils {

    private static final ClassPool CLASS_POOL = ClassPool.getDefault();

    public static CtClass getCtClass(String className) throws NotFoundException {
        return CLASS_POOL.get(className);
    }

    public static CtClass[] constructParams(String... classNames) throws NotFoundException {
        CtClass[] params = new CtClass[classNames.length];
        for (int i = 0; i < classNames.length; i++) {
            params[i] = getCtClass(classNames[i]);
        }
        return params;
    }

}
