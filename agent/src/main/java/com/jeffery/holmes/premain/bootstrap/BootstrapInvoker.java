package com.jeffery.holmes.premain.bootstrap;

import com.jeffery.holmes.premain.classloader.ContextClassloaderExecutor;

import java.lang.instrument.Instrumentation;
import java.lang.reflect.Method;

public class BootstrapInvoker implements Bootstrap {

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
        ContextClassloaderExecutor contextClassloaderExecutor = new ContextClassloaderExecutor(classLoader);
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
