package com.jeffery.holmes.core.collect;

import com.jeffery.holmes.core.base.ScheduledTask;

import java.util.concurrent.TimeUnit;

public class CollectTask extends ScheduledTask {

    @Override
    protected Runnable getTask() {
        return null;
    }

    @Override
    protected long getInitialDelay() {
        return 60;
    }

    @Override
    protected long getPeriod() {
        return 120;
    }

    @Override
    protected TimeUnit getTimeUnit() {
        return TimeUnit.SECONDS;
    }

}
