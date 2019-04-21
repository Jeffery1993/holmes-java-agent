package com.jeffery.holmes.core.collect;

import com.jeffery.holmes.common.collector.Collector;
import com.jeffery.holmes.core.base.ScheduledTask;

import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

public class CollectTask extends ScheduledTask {

    private static final CollectTask COLLECT_TASK = new CollectTask();

    private CollectTask() {
    }

    public static CollectTask getInstance() {
        return COLLECT_TASK;
    }

    @Override
    protected Runnable getTask() {
        return new Runnable() {
            @Override
            public void run() {
                List<Collector> collectors = CollectorManager.getCollectors();
                for (Collector collector : collectors) {
                    if (collector.isEnabled()) {
                        Map<String, Object> res = collector.collect();
                        DataQueueService.getInstance().offer(res);
                    }
                }
            }
        };
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
