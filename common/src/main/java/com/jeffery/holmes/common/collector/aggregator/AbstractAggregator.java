package com.jeffery.holmes.common.collector.aggregator;

/**
 * Abstract class for {@link Aggregator}.
 *
 * @see com.jeffery.holmes.common.collector.aggregator.Aggregator
 */
public abstract class AbstractAggregator implements Aggregator {

    protected volatile boolean enabled = true;

    @Override
    public boolean isEnabled() {
        return enabled;
    }

    @Override
    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

}
