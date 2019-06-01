package com.jeffery.holmes.common.collector;

import java.util.Map;

/**
 * Interface for collector. A collector consists of one or more components.
 *
 * @param <T> class of components
 */
public interface Collector<T> {

    /**
     * Get the name of the collector.
     *
     * @return name
     */
    String getName();

    /**
     * Check whether the collecor is enabled or not.
     *
     * @return enabled status
     */
    boolean isEnabled();

    /**
     * Set the enabled status of the collector.
     *
     * @param enabled new status
     */
    void setEnabled(boolean enabled);

    /**
     * Add a new part to the collector.
     *
     * @param item new part to be added
     * @return the collector
     */
    Collector add(T item);

    /**
     * Get the data of the collector.
     *
     * @return data
     */
    Map<String, Object> collect();

}
