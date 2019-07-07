package com.jeffery.holmes.common.collector.aggregator;

import java.util.List;
import java.util.Map;

/**
 * Interface for aggregator.
 *
 * <p>Aggregators are components for Collector.</p>
 */
public interface Aggregator {

    /**
     * Get the name of aggregator.
     *
     * @return name
     */
    String getName();

    /**
     * Get the enabled status of aggregator.
     *
     * @return enable status
     */
    boolean isEnabled();

    /**
     * Set the enabled status of aggregator.
     *
     * @param enabled
     */
    void setEnabled(boolean enabled);

    /**
     * Get the data of the aggregator.
     *
     * @return data
     */
    List<Map<String, Object>> harvest();

}
