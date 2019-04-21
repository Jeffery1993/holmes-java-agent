package com.jeffery.holmes.common.collector;

import com.jeffery.holmes.common.collector.aggregator.Aggregator;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public abstract class AbstractCollector implements Collector<Aggregator> {

    protected List<Aggregator> aggregators = new ArrayList<Aggregator>();

    @Override
    public Collector add(Aggregator aggregator) {
        aggregators.add(aggregator);
        return this;
    }

    @Override
    public Map<String, Object> collect() {
        Map<String, Object> map = new LinkedHashMap<String, Object>();
        for (Aggregator aggregator : aggregators) {
            map.put(aggregator.getName(), aggregator.harvest());
        }
        return map;
    }

}
