package com.jeffery.holmes.common.plugin.jvm;

import com.jeffery.holmes.common.collector.aggregator.AbstractAggregator;

import java.lang.management.GarbageCollectorMXBean;
import java.lang.management.ManagementFactory;
import java.util.*;

public class JVMGCAggregator extends AbstractAggregator {

    private GarbageCollectorMXBean ygcMXBean;
    private GarbageCollectorMXBean fgcMXBean;

    private Long lastYgcCount;
    private Long lastYgcTime;
    private Long lastFgcCount;
    private Long lastFgcTime;

    public JVMGCAggregator() {
        for (GarbageCollectorMXBean garbageCollectorMXBean : ManagementFactory.getGarbageCollectorMXBeans()) {
            if (YGC_NAME_SET.contains(garbageCollectorMXBean.getName())) {
                ygcMXBean = garbageCollectorMXBean;
                lastYgcCount = ygcMXBean.getCollectionCount();
                lastYgcTime = ygcMXBean.getCollectionTime();
            } else if (FGC_NAME_SET.contains(garbageCollectorMXBean.getName())) {
                fgcMXBean = garbageCollectorMXBean;
                lastFgcCount = fgcMXBean.getCollectionCount();
                lastFgcTime = fgcMXBean.getCollectionTime();
            }
        }
    }

    @Override
    public String getName() {
        return "gc";
    }

    @Override
    public List<Map<String, Object>> harvest() {
        List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("ygcCount", getYgcCount());
        map.put("ygcTime", getYgcTime());
        map.put("fgcCount", getFgcCount());
        map.put("fgcTime", getFgcTime());
        list.add(map);
        return list;
    }

    private long getYgcCount() {
        if (ygcMXBean == null || lastYgcCount == null) {
            return -1;
        }
        long ygcCount = ygcMXBean.getCollectionCount() - lastYgcCount;
        lastYgcCount = ygcMXBean.getCollectionCount();
        return ygcCount;
    }

    private long getYgcTime() {
        if (ygcMXBean == null || lastYgcTime == null) {
            return -1;
        }
        long ygcTime = ygcMXBean.getCollectionTime() - lastYgcTime;
        lastYgcTime = ygcMXBean.getCollectionTime();
        return ygcTime;
    }

    private long getFgcCount() {
        if (fgcMXBean == null || lastFgcCount == null) {
            return -1;
        }
        long fgcCount = fgcMXBean.getCollectionCount() - lastFgcCount;
        lastFgcCount = fgcMXBean.getCollectionCount();
        return fgcCount;
    }

    private long getFgcTime() {
        if (fgcMXBean == null || lastFgcTime == null) {
            return -1;
        }
        long fgcTime = fgcMXBean.getCollectionTime() - lastFgcTime;
        lastFgcTime = fgcMXBean.getCollectionTime();
        return fgcTime;
    }

    private static final Set<String> YGC_NAME_SET = new HashSet<String>();
    private static final Set<String> FGC_NAME_SET = new HashSet<String>();

    static {
        YGC_NAME_SET.add("PS Scavenge");
        FGC_NAME_SET.add("PS MarkSweep");
    }

}
