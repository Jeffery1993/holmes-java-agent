package com.jeffery.holmes.core.collect;

import com.jeffery.holmes.common.collector.Collector;
import com.jeffery.holmes.common.util.ConfigManager;
import com.jeffery.holmes.core.base.AbstractScheduledTask;

import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;

public class CollectTask extends AbstractScheduledTask {

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
                logger.info("Enter collect task");
                List<Collector> collectors = CollectorManager.getCollectors();
                for (Collector collector : collectors) {
                    if (collector.isEnabled()) {
                        try {
                            Map<String, Object> res = collector.collect();
                            DataQueueService.getInstance().offer(res);
                        } catch (Throwable throwable) {
                            logger.log(Level.SEVERE, "Failed to collect data from [" + collector.getName() + "], " + throwable.getMessage(), throwable);
                            collector.setEnabled(false);
                        }
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
        return ConfigManager.getCollectInterval();
    }

    @Override
    protected TimeUnit getTimeUnit() {
        return TimeUnit.SECONDS;
    }

}
