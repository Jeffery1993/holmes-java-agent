package com.jeffery.holmes.common.plugin.jvm;

import com.jeffery.holmes.common.collector.AbstractCollector;
import com.jeffery.holmes.common.collector.Collector;

public class JVMInfoCollector extends AbstractCollector {

    private JVMOSAggregator jvmOsAggregator = new JVMOSAggregator();
    private JVMRuntimeAggregator jvmRuntimeAggregator = new JVMRuntimeAggregator();

    private JVMInfoCollector() {
        this.add(jvmOsAggregator);
        this.add(jvmRuntimeAggregator);
    }

    public static Collector getInstance() {
        return new JVMInfoCollector();
    }

}
