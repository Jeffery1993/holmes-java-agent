package com.jeffery.holmes.premain.bootstrap;

import com.jeffery.holmes.premain.classloader.ContextClassloaderExecutor;

import java.lang.instrument.Instrumentation;

public class BootstrapInvoker implements Bootstrap {

    private static final String BOOTSTRAP_IMPL_CLASS = "com.jeffery.holmes.core.BootstrapImpl";

    private ClassLoader classLoader;

    public BootstrapInvoker(ClassLoader classLoader) {
        this.classLoader = classLoader;
    }

    @Override
    public void boot(final String args, final Instrumentation instrumentation) {
        Class<?> bootstrapClazz = null;
        Object bootstrapInstance = null;
        ContextClassloaderExecutor contextClassloaderExecutor = new ContextClassloaderExecutor(classLoader);
        try {
            bootstrapClazz = classLoader.loadClass(BOOTSTRAP_IMPL_CLASS);
            bootstrapInstance = bootstrapClazz.newInstance();
        } catch (Exception e) {
            e.printStackTrace();
            return;
        }
        if (!(bootstrapInstance instanceof Bootstrap)) {
            return;
        }
        final Bootstrap bootstrap = (Bootstrap) bootstrapInstance;
        contextClassloaderExecutor.execute(new Runnable() {
            @Override
            public void run() {
                bootstrap.boot(args, instrumentation);
            }
        });
    }

}
