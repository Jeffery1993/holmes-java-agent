package com.jeffery.holmes.core.collect;

import com.jeffery.holmes.common.collector.Collector;

import java.util.ArrayList;
import java.util.List;

public class CollectorManager {

    private static final List<Collector> COLLECTORS = new ArrayList<Collector>();

    public static void register(Collector collector) {
        COLLECTORS.add(collector);
    }

    public static List<Collector> getCollectors() {
        return COLLECTORS;
    }

}
