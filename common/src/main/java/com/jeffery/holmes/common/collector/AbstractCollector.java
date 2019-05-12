package com.jeffery.holmes.common.collector;

import com.jeffery.holmes.common.collector.aggregator.Aggregator;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public abstract class AbstractCollector implements Collector<Aggregator> {

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
        for (Aggregator aggregator : aggregators) {
            if (aggregator.isEnabled()) {
                map.put(aggregator.getName(), aggregator.harvest());
            }
        }
        return map;
    }

}
