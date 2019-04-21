package com.jeffery.holmes.premain.classloader;

import java.util.concurrent.Callable;

public class ContextClassloaderExecutor {

    private ClassLoader contextClassLoader;

    public ContextClassloaderExecutor(ClassLoader contextClassLoader) {
        this.contextClassLoader = contextClassLoader;
    }

    public void execute(Runnable runnable) {
        final Thread currentThread = Thread.currentThread();
        final ClassLoader before = currentThread.getContextClassLoader();
        currentThread.setContextClassLoader(contextClassLoader);
        try {
            runnable.run();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            currentThread.setContextClassLoader(before);
        }
    }

    public <T> T execute(Callable<T> callable) {
        final Thread currentThread = Thread.currentThread();
        final ClassLoader before = currentThread.getContextClassLoader();
        currentThread.setContextClassLoader(contextClassLoader);
        T res = null;
        try {
            res = callable.call();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            currentThread.setContextClassLoader(before);
        }
        return res;
    }

}
