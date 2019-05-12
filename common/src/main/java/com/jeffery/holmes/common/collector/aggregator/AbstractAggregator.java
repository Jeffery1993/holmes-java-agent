package com.jeffery.holmes.common.collector.aggregator;

public abstract class AbstractAggregator implements Aggregator {

    protected volatile boolean enabled = true;

    @Override
    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

}
