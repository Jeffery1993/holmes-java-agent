package com.jeffery.holmes.common.collector.stats;

import com.jeffery.holmes.common.util.ConcurrentUtils;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

/**
 * Base class for stats.
 */
public class BaseStats implements Stats {

    protected static final long RADIX = 1000000L;

    // count of invocations
    private AtomicInteger invokeCount = new AtomicInteger(0);
    // count of errors
    private AtomicInteger errorCount = new AtomicInteger(0);
    // total time
    private AtomicLong totalTime = new AtomicLong(0L);
    // maximum time
    private AtomicLong maxTime = new AtomicLong(0L);
    // count of running tasks
    private AtomicInteger runningCount = new AtomicInteger(0);
    // maximum of concurrent tasks
    private AtomicInteger concurrentMax = new AtomicInteger(0);

    @Override
    public long onStart() {
        long startTime = System.nanoTime();
        ConcurrentUtils.setMaxConcurrently(concurrentMax, runningCount.incrementAndGet());
        return startTime;
    }

    @Override
    public void onThrowable(Throwable throwable) {
        errorCount.getAndIncrement();
    }

    @Override
    public long onFinally(long startTime) {
        invokeCount.getAndIncrement();
        runningCount.getAndDecrement();
        long usedTime = System.nanoTime() - startTime;
        totalTime.getAndAdd(usedTime);
        ConcurrentUtils.setMaxConcurrently(maxTime, usedTime);
        return usedTime;
    }

    @Override
    public Map<String, Object> harvest() {
        Map<String, Object> map = new LinkedHashMap<String, Object>();
        map.put("invokeCount", invokeCount.getAndSet(0));
        map.put("errorCount", errorCount.getAndSet(0));
        map.put("totalTime", totalTime.getAndSet(0L) / RADIX);
        map.put("maxTime", maxTime.getAndSet(0L) / RADIX);
        map.put("runningCount", runningCount.getAndSet(0));
        map.put("concurrentMax", concurrentMax.getAndSet(0));
        return map;
    }

}
