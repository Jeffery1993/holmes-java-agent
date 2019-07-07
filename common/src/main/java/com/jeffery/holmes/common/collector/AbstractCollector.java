package com.jeffery.holmes.common.collector;

import com.jeffery.holmes.common.collector.aggregator.Aggregator;
import com.jeffery.holmes.common.util.LoggerFactory;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Abstract class for {@link Collector}. A collector consists of one or more {@link Aggregator}(s).
 */
public abstract class AbstractCollector implements Collector<Aggregator> {

    protected final Logger logger = LoggerFactory.getLogger(this.getClass());

    protected String name;
    protected boolean enabled = true;
    protected List<Aggregator> aggregators = new ArrayList<Aggregator>();

    @Override
    public String getName() {
        if (this.name == null) {
            this.name = this.getClass().getSimpleName().replace("Collector", "");
        }
        return this.name;
    }

    @Override
    public boolean isEnabled() {
        return enabled;
    }

    @Override
    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    @Override
    public Collector add(Aggregator aggregator) {
        aggregators.add(aggregator);
        return this;
    }

    @Override
    public Map<String, Object> collect() {
        Map<String, Object> map = new LinkedHashMap<String, Object>();
        // add timestamp to the collected data
        map.put("name", getName());
        map.put("timestamp", System.currentTimeMillis());
        // collect data from aggregators
        for (Aggregator aggregator : aggregators) {
            if (aggregator.isEnabled()) {
                try {
                    map.put(aggregator.getName(), aggregator.harvest());
                } catch (Throwable throwable) {
                    logger.log(Level.SEVERE, "Failed to harvest data from [" + aggregator.getName() + "], " + throwable.getMessage(), throwable);
                    aggregator.setEnabled(false);
                }
            }
        }
        return map;
    }

}
