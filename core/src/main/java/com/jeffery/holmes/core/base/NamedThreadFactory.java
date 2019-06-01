package com.jeffery.holmes.core.base;

import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Thread factory that can have user-defined names.
 */
public class NamedThreadFactory implements ThreadFactory {

    private final AtomicInteger threadNumber = new AtomicInteger(1);
    private final String namePrefix;
    private final boolean daemon;

    public NamedThreadFactory(String namePrefix) {
        this(namePrefix, true);
    }

    public NamedThreadFactory(String namePrefix, boolean daemon) {
        this.namePrefix = namePrefix;
        this.daemon = daemon;
    }

    @Override
    public Thread newThread(Runnable runnable) {
        Thread t = new Thread(runnable, namePrefix + threadNumber.getAndIncrement());
        t.setDaemon(daemon);
        return t;
    }

}
