package com.jeffery.holmes.common.collector.stats;

import com.jeffery.holmes.common.util.ConcurrentUtils;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

public class BaseStats implements Stats {

    // 调用次数
    private AtomicInteger invokeCount = new AtomicInteger(0);
    // 错误次数
    private AtomicInteger errorCount = new AtomicInteger(0);
    // 总共耗时
    private AtomicLong totalTime = new AtomicLong(0L);
    // 最大耗时
    private AtomicLong maxTime = new AtomicLong(0L);
    // 当前并发
    private AtomicInteger runningCount = new AtomicInteger(0);
    // 最大并发
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
        map.put("invokeCount", invokeCount.get());
        map.put("errorCount", errorCount.get());
        map.put("totalTime", totalTime.get());
        map.put("maxTime", maxTime.get());
        map.put("runningCount", runningCount.get());
        map.put("concurrentMax", concurrentMax.get());
        return map;
    }

}
