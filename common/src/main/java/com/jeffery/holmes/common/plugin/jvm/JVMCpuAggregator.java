package com.jeffery.holmes.common.plugin.jvm;

import com.jeffery.holmes.common.collector.aggregator.AbstractAggregator;

import javax.management.MBeanServer;
import javax.management.ObjectName;
import java.lang.management.ManagementFactory;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class JVMCpuAggregator extends AbstractAggregator {

    private final MBeanServer server;
    private final ObjectName objectName;
    private final int availableProcessors;

    private Long lastProcessCpuTime;
    private long lastSystemTime;

    public JVMCpuAggregator() throws Exception {
        server = ManagementFactory.getPlatformMBeanServer();
        objectName = new ObjectName("java.lang:type=OperatingSystem");
        availableProcessors = getAvailableProcessors();
        lastProcessCpuTime = getProcessCpuTime();
        lastSystemTime = System.nanoTime();
    }

    private int getAvailableProcessors() throws Exception {
        Object object = server.getAttribute(objectName, "AvailableProcessors");
        return Integer.parseInt(object.toString());
    }

    private long getProcessCpuTime() throws Exception {
        Object object = server.getAttribute(objectName, "ProcessCpuTime");
        return Long.parseLong(object.toString());
    }

    @Override
    public String getName() {
        return "cpu";
    }

    @Override
    public List<Map<String, Object>> harvest() {
        List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("cpuRatio", getCpuRatio());
        list.add(map);
        return list;
    }

    private double getCpuRatio() {
        Long nowProcessCpuTime = null;
        try {
            nowProcessCpuTime = getProcessCpuTime();
        } catch (Exception e) {
        }
        if (nowProcessCpuTime == null || lastProcessCpuTime == null) {
            return -1;
        }
        long nowSystemTime = System.nanoTime();
        double cpuRatio = (double) (nowProcessCpuTime - lastProcessCpuTime) / (nowSystemTime - lastSystemTime) / availableProcessors * 100;
        lastProcessCpuTime = nowProcessCpuTime;
        lastSystemTime = nowSystemTime;
        return cpuRatio;
    }

}
