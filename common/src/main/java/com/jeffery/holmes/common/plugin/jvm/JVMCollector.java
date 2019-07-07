package com.jeffery.holmes.common.plugin.jvm;

import com.jeffery.holmes.common.collector.AbstractCollector;
import com.jeffery.holmes.common.collector.Collector;

public class JVMCollector extends AbstractCollector {

    private JVMCollector() {
        this.add(new JVMGCAggregator());
        try {
            this.add(new JVMCpuAggregator());
        } catch (Exception e) {
        }
        this.add(new JVMMemoryAggregator());
        this.add(new JVMMemoryPoolAggregator());
        this.add(new JVMThreadAggregator());
        this.add(new JVMClassLoadingAggregator());
    }

    private static final JVMCollector INSTANCE = new JVMCollector();

    public static Collector getInstance() {
        return INSTANCE;
    }

}
