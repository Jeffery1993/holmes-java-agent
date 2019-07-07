package com.jeffery.holmes.core.base;

import com.jeffery.holmes.common.util.LoggerFactory;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.logging.Logger;

/**
 * Abstract class for scheduled task.
 */
public abstract class AbstractScheduledTask {

    protected final Logger logger = LoggerFactory.getLogger(this.getClass());

    private ScheduledExecutorService scheduledExecutorService = Executors.newScheduledThreadPool(1);

    /**
     * Start to be executed periodically with a initial delay.
     */
    public void execute() {
        scheduledExecutorService.scheduleAtFixedRate(getTask(), getInitialDelay(), getPeriod(), getTimeUnit());
    }

    /**
     * Shut down the executor service.
     */
    public void shutdown() {
        scheduledExecutorService.shutdown();
    }

    /**
     * Get the task to be executed.
     *
     * @return the task to be executed
     */
    protected abstract Runnable getTask();

    /**
     * Get the initial delay.
     *
     * @return the initial delay
     */
    protected abstract long getInitialDelay();

    /**
     * Get the period.
     *
     * @return the period
     */
    protected abstract long getPeriod();

    /**
     * Get the time unit of the initial delay and period.
     *
     * @return the time unit
     */
    protected abstract TimeUnit getTimeUnit();

}
