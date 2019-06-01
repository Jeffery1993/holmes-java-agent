package com.jeffery.holmes.agent.bootstrap;

import com.jeffery.holmes.agent.classloader.ContextClassLoaderExecutor;

import java.lang.instrument.Instrumentation;
import java.lang.reflect.Method;

/**
 * Proxy class for agent boot class.
 */
public class BootstrapInvoker implements Bootstrap {

    /**
     * The actual class name for boot class.
     */
    private static final String BOOTSTRAP_IMPL_CLASS = "com.jeffery.holmes.core.BootstrapImpl";

    private ClassLoader classLoader;

    public BootstrapInvoker(ClassLoader classLoader) {
        this.classLoader = classLoader;
    }

    @Override
    public void boot(final String args, final Instrumentation instrumentation) {
        final Class<?> bootstrapClazz;
        final Object bootstrapInstance;
        final Method bootMethod;
        ContextClassLoaderExecutor contextClassloaderExecutor = new ContextClassLoaderExecutor(classLoader);
        try {
            bootstrapClazz = classLoader.loadClass(BOOTSTRAP_IMPL_CLASS);
            bootstrapInstance = bootstrapClazz.newInstance();
            bootMethod = bootstrapClazz.getMethod("boot", String.class, Instrumentation.class);
        } catch (Exception e) {
            e.printStackTrace();
            return;
        }

        contextClassloaderExecutor.execute(new Runnable() {
            @Override
            public void run() {
                try {
                    bootMethod.invoke(bootstrapInstance, args, instrumentation);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

}
