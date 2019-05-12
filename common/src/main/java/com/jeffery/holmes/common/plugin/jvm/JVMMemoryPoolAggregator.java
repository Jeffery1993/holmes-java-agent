package com.jeffery.holmes.common.plugin.jvm;

import com.jeffery.holmes.common.collector.aggregator.AbstractAggregator;

import java.lang.management.ManagementFactory;
import java.lang.management.MemoryPoolMXBean;
import java.lang.management.MemoryUsage;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class JVMMemoryPoolAggregator extends AbstractAggregator {

    private List<MemoryPoolMXBean> memoryPoolMXBeans;

    public JVMMemoryPoolAggregator() {
        memoryPoolMXBeans = ManagementFactory.getMemoryPoolMXBeans();
    }

    @Override
    public String getName() {
        return "memoryPool";
    }

    @Override
    public List<Map<String, Object>> harvest() {
        List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
        for (MemoryPoolMXBean memoryPoolMXBean : memoryPoolMXBeans) {
            Map<String, Object> map = new HashMap<String, Object>();
            map.put("name", memoryPoolMXBean.getName());
            MemoryUsage memoryUsage = memoryPoolMXBean.getUsage();
            map.put("init", memoryUsage.getInit());
            map.put("used", memoryUsage.getUsed());
            map.put("committed", memoryUsage.getCommitted());
            map.put("max", memoryUsage.getMax());
            list.add(map);
        }
        return list;
    }

}
