package com.jeffery.holmes.core.base;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public abstract class ScheduledTask {

    private ScheduledExecutorService scheduledExecutorService = Executors.newScheduledThreadPool(1);

    public void execute() {
        scheduledExecutorService.scheduleAtFixedRate(getTask(), getInitialDelay(), getPeriod(), getTimeUnit());
    }

    public void shutdown() {
        scheduledExecutorService.shutdown();
    }

    protected abstract Runnable getTask();

    protected abstract long getInitialDelay();

    protected abstract long getPeriod();

    protected abstract TimeUnit getTimeUnit();

}
