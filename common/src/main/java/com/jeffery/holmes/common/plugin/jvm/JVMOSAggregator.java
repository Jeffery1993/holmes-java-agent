package com.jeffery.holmes.common.plugin.jvm;

import com.jeffery.holmes.common.collector.aggregator.NonePrimaryKeyAggregator;

import java.lang.management.ManagementFactory;
import java.lang.management.OperatingSystemMXBean;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class JVMOSAggregator extends NonePrimaryKeyAggregator {

    private OperatingSystemMXBean operatingSystemMXBean;

    public JVMOSAggregator() {
        operatingSystemMXBean = ManagementFactory.getOperatingSystemMXBean();
    }

    @Override
    public String getName() {
        return "os";
    }

    @Override
    public List<Map<String, Object>> harvest() {
        List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("name", operatingSystemMXBean.getName());
        map.put("version", operatingSystemMXBean.getVersion());
        map.put("arch", operatingSystemMXBean.getArch());
        map.put("availableProcessors", operatingSystemMXBean.getAvailableProcessors());
        list.add(map);
        return list;
    }

}
