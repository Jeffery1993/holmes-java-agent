package com.jeffery.holmes.agent.classloader;

import java.net.URL;
import java.net.URLClassLoader;

/**
 * Classloader used for agent exclusively.
 */
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
        // load from parent first
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
