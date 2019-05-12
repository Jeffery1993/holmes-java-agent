package com.jeffery.holmes.common.plugin.jvm;

import com.jeffery.holmes.common.collector.aggregator.AbstractAggregator;

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
        map.put("classPath", runtimeMXBean.getClassPath());
        map.put("libraryPath", runtimeMXBean.getLibraryPath());
        map.put("bootClassPath", runtimeMXBean.getBootClassPath());
        map.put("inputArguments", runtimeMXBean.getInputArguments());
        map.put("startTime", runtimeMXBean.getStartTime());
        map.put("vmName", runtimeMXBean.getVmName());
        map.put("vmVendor", runtimeMXBean.getVmVendor());
        map.put("vmVersion", runtimeMXBean.getVmVersion());
        map.put("specName", runtimeMXBean.getSpecName());
        map.put("specVendor", runtimeMXBean.getSpecVendor());
        map.put("specVersion", runtimeMXBean.getSpecVersion());
        list.add(map);
        return list;
    }

}
