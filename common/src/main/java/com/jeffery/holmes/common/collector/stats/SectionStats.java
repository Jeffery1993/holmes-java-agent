package com.jeffery.holmes.common.collector.stats;

import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

public class SectionStats extends BaseStats {

    private AtomicInteger ms0_1 = new AtomicInteger(0);
    private AtomicInteger ms1_10 = new AtomicInteger(0);
    private AtomicInteger ms10_100 = new AtomicInteger(0);
    private AtomicInteger ms100_1000 = new AtomicInteger(0);
    private AtomicInteger s1_10 = new AtomicInteger(0);
    private AtomicInteger s10_n = new AtomicInteger(0);

    @Override
    public long onFinally(long startTime) {
        long usedTime = super.onFinally(startTime);
        long usedTimeInMs = usedTime / 1000000L;
        if (usedTimeInMs <= 1L) {
            ms0_1.getAndIncrement();
        } else if (usedTimeInMs <= 10L) {
            ms1_10.getAndIncrement();
        } else if (usedTimeInMs <= 100L) {
            ms10_100.getAndIncrement();
        } else if (usedTimeInMs <= 1000L) {
            ms100_1000.getAndIncrement();
        } else if (usedTimeInMs <= 10000L) {
            s1_10.getAndIncrement();
        } else {
            s10_n.getAndIncrement();
        }
        return usedTime;
    }

    @Override
    public Map<String, Object> harvest() {
        Map<String, Object> map = super.harvest();
        map.put("ms0_1", ms0_1.get());
        map.put("ms1_10", ms1_10.get());
        map.put("ms10_100", ms10_100.get());
        map.put("ms100_1000", ms100_1000.get());
        map.put("s1_10", s1_10.get());
        map.put("s10_n", s10_n.get());
        return map;
    }

}
