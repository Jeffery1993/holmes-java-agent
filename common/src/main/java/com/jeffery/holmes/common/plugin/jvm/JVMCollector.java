package com.jeffery.holmes.common.plugin.jvm;

import com.jeffery.holmes.common.collector.AbstractCollector;
import com.jeffery.holmes.common.collector.Collector;

public class JVMCollector extends AbstractCollector {

    private JVMGCAggregator jvmGcAggregator = new JVMGCAggregator();
    private JVMMemoryAggregator jvmMemoryAggregator = new JVMMemoryAggregator();
    private JVMMemoryPoolAggregator jvmMemoryPoolAggregator = new JVMMemoryPoolAggregator();
    private JVMThreadAggregator jvmThreadAggregator = new JVMThreadAggregator();
    private JVMClassLoadingAggregator jvmClassLoadingAggregator = new JVMClassLoadingAggregator();
    private JVMCompilationAggregator jvmCompilationAggregator = new JVMCompilationAggregator();

    private JVMCollector() {
        this.add(jvmGcAggregator);
        this.add(jvmMemoryAggregator);
        this.add(jvmMemoryPoolAggregator);
        this.add(jvmThreadAggregator);
        this.add(jvmClassLoadingAggregator);
        this.add(jvmCompilationAggregator);
    }

    public static Collector getInstance() {
        return new JVMCollector();
    }

}
