package com.jeffery.holmes.common.collector.aggregator;

public abstract class AbstractAggregator implements Aggregator {

    private boolean enabled;
    private boolean overflow;

    @Override
    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    @Override
    public boolean isOverflow() {
        return overflow;
    }

    public void setOverflow(boolean overflow) {
        this.overflow = overflow;
    }

}
