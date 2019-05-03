package com.jeffery.holmes.common.plugin.jvm;

import com.jeffery.holmes.common.collector.aggregator.NonePrimaryKeyAggregator;

import java.lang.management.ManagementFactory;
import java.lang.management.ThreadMXBean;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class JVMThreadAggregator extends NonePrimaryKeyAggregator {

    private ThreadMXBean threadMXBean;

    public JVMThreadAggregator() {
        threadMXBean = ManagementFactory.getThreadMXBean();
    }

    @Override
    public String getName() {
        return "thread";
    }

    @Override
    public List<Map<String, Object>> harvest() {
        List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("threadCount", threadMXBean.getThreadCount());
        map.put("peakThreadCount", threadMXBean.getPeakThreadCount());
        map.put("daemonThreadCount", threadMXBean.getDaemonThreadCount());
        list.add(map);
        return list;
    }

}
