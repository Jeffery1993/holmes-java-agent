package com.jeffery.holmes.common.plugin.jvm;

import com.jeffery.holmes.common.collector.aggregator.AbstractAggregator;

import java.lang.management.GarbageCollectorMXBean;
import java.lang.management.ManagementFactory;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class JVMGCAggregator extends AbstractAggregator {

    private List<GarbageCollectorMXBean> garbageCollectorMXBeans;

    public JVMGCAggregator() {
        garbageCollectorMXBeans = ManagementFactory.getGarbageCollectorMXBeans();
    }

    @Override
    public String getName() {
        return "gc";
    }

    @Override
    public List<Map<String, Object>> harvest() {
        List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
        for (GarbageCollectorMXBean garbageCollectorMXBean : garbageCollectorMXBeans) {
            Map<String, Object> map = new HashMap<String, Object>();
            map.put("name", garbageCollectorMXBean.getName());
            map.put("collectionCount", garbageCollectorMXBean.getCollectionCount());
            map.put("collectionTime", garbageCollectorMXBean.getCollectionTime());
            list.add(map);
        }
        return list;
    }

}
