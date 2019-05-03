package com.jeffery.holmes.core.collect;

import com.jeffery.holmes.common.collector.Collector;
import com.jeffery.holmes.common.plugin.jvm.JVMCollector;
import com.jeffery.holmes.common.plugin.jvm.JVMInfoCollector;

import java.util.ArrayList;
import java.util.List;

public class CollectorManager {

    private static final List<Collector> COLLECTORS = new ArrayList<Collector>();

    static {
        COLLECTORS.add(JVMCollector.getInstance());
        COLLECTORS.add(JVMInfoCollector.getInstance());
    }

    public static void register(Collector collector) {
        COLLECTORS.add(collector);
    }

    public static List<Collector> getCollectors() {
        return COLLECTORS;
    }

}
