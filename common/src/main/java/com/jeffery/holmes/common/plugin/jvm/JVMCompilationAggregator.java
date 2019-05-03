package com.jeffery.holmes.common.plugin.jvm;

import com.jeffery.holmes.common.collector.aggregator.NonePrimaryKeyAggregator;

import java.lang.management.CompilationMXBean;
import java.lang.management.ManagementFactory;
import java.lang.management.OperatingSystemMXBean;
import java.lang.management.RuntimeMXBean;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class JVMCompilationAggregator extends NonePrimaryKeyAggregator {

    private CompilationMXBean compilationMXBean;

    public JVMCompilationAggregator() {
        compilationMXBean = ManagementFactory.getCompilationMXBean();
        OperatingSystemMXBean operatingSystemMXBean = ManagementFactory.getOperatingSystemMXBean();
        RuntimeMXBean runtimeMXBean = ManagementFactory.getRuntimeMXBean();
    }

    @Override
    public String getName() {
        return "compilation";
    }

    @Override
    public List<Map<String, Object>> harvest() {
        List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("name", compilationMXBean.getName());
        map.put("totalCompilationTime", compilationMXBean.getTotalCompilationTime());
        list.add(map);
        return list;
    }

}
