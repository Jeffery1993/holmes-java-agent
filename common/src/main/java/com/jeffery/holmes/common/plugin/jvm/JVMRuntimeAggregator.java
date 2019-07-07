package com.jeffery.holmes.common.plugin.jvm;

import com.jeffery.holmes.common.collector.aggregator.AbstractAggregator;
import com.jeffery.holmes.common.util.DateUtils;

import java.lang.management.ManagementFactory;
import java.lang.management.RuntimeMXBean;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class JVMRuntimeAggregator extends AbstractAggregator {

    private RuntimeMXBean runtimeMXBean;

    public JVMRuntimeAggregator() {
        runtimeMXBean = ManagementFactory.getRuntimeMXBean();
    }

    @Override
    public String getName() {
        return "runtime";
    }

    @Override
    public List<Map<String, Object>> harvest() {
        List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("name", runtimeMXBean.getName());
        map.put("vmName", runtimeMXBean.getVmName());
        map.put("vmVendor", runtimeMXBean.getVmVendor());
        map.put("vmVersion", runtimeMXBean.getVmVersion());
        map.put("startTime", DateUtils.format(runtimeMXBean.getStartTime()));
        map.put("inputArguments", runtimeMXBean.getInputArguments());
        list.add(map);
        return list;
    }

}
