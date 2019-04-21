package com.jeffery.holmes.premain.classloader;

import java.net.URL;
import java.net.URLClassLoader;

public class AgentClassLoader extends URLClassLoader {

    public AgentClassLoader(URL[] urls) {
        super(urls, ClassLoader.getSystemClassLoader());
    }

    @Override
    protected synchronized Class<?> loadClass(String name, boolean resolve) throws ClassNotFoundException {
        final Class<?> loadedClass = findLoadedClass(name);
        if (loadedClass != null) {
            return loadedClass;
        }

        // 优先从parent里加载系统类，避免抛出ClassNotFoundException
        if (name != null && (name.startsWith("sun.") || name.startsWith("java."))) {
            return super.loadClass(name, resolve);
        }
        try {
            Class<?> clazz = findClass(name);
            if (resolve) {
                resolveClass(clazz);
            }
            return clazz;
        } catch (Exception e) {
            // ignore
        }
        return super.loadClass(name, resolve);
    }

}
