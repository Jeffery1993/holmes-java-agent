package com.jeffery.holmes.common.plugin.jvm;

import com.jeffery.holmes.common.collector.aggregator.NonePrimaryKeyAggregator;

import java.lang.management.ManagementFactory;
import java.lang.management.MemoryMXBean;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class JVMMemoryAggregator extends NonePrimaryKeyAggregator {

    private MemoryMXBean memoryMXBean;

    public JVMMemoryAggregator() {
        memoryMXBean = ManagementFactory.getMemoryMXBean();
    }

    @Override
    public String getName() {
        return "memory";
    }

    @Override
    public List<Map<String, Object>> harvest() {
        List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("heapMemoryUsage", memoryMXBean.getHeapMemoryUsage());
        map.put("nonHeapMemoryUsage", memoryMXBean.getNonHeapMemoryUsage());
        list.add(map);
        return list;
    }

}
