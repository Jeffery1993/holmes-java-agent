package com.jeffery.holmes.agent.classloader;

import java.util.concurrent.Callable;

/**
 * Execute class for ContextClassLoader.
 */
public class ContextClassLoaderExecutor {

    private ClassLoader contextClassLoader;

    public ContextClassLoaderExecutor(ClassLoader contextClassLoader) {
        this.contextClassLoader = contextClassLoader;
    }

    /**
     * Execute {@link Runnable} work.
     *
     * @param runnable runnable task
     */
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

    /**
     * Execute {@link Callable} work.
     *
     * @param callable callable task
     * @param <T>      type of callable task
     * @return result
     */
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
