package com.jeffery.holmes.common.plugin.jvm;

import com.jeffery.holmes.common.collector.AbstractCollector;
import com.jeffery.holmes.common.collector.Collector;

public class JVMInfoCollector extends AbstractCollector {

    private JVMInfoCollector() {
        this.add(new JVMOSAggregator());
        this.add(new JVMRuntimeAggregator());
    }

    private static final JVMInfoCollector INSTANCE = new JVMInfoCollector();

    public static Collector getInstance() {
        return INSTANCE;
    }

}
