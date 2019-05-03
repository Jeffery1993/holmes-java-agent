package com.jeffery.holmes.common.plugin.jvm;

import com.jeffery.holmes.common.collector.aggregator.NonePrimaryKeyAggregator;

import java.lang.management.ClassLoadingMXBean;
import java.lang.management.ManagementFactory;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class JVMClassLoadingAggregator extends NonePrimaryKeyAggregator {

    private ClassLoadingMXBean classLoadingMXBean;

    public JVMClassLoadingAggregator() {
        classLoadingMXBean = ManagementFactory.getClassLoadingMXBean();
    }

    @Override
    public String getName() {
        return "classLoading";
    }

    @Override
    public List<Map<String, Object>> harvest() {
        List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("loadedClassCount", classLoadingMXBean.getLoadedClassCount());
        map.put("totalLoadedClassCount", classLoadingMXBean.getTotalLoadedClassCount());
        map.put("unloadedClassCount", classLoadingMXBean.getUnloadedClassCount());
        list.add(map);
        return list;
    }

}
