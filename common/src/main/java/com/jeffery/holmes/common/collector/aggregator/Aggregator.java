package com.jeffery.holmes.common.collector.aggregator;

import java.util.List;
import java.util.Map;

public interface Aggregator {

    String getName();

    boolean isEnabled();

    boolean isOverflow();

    List<Map<String, Object>> harvest();

}
