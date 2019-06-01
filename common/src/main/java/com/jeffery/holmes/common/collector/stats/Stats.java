package com.jeffery.holmes.common.collector.stats;

import java.util.Map;

/**
 * Interface for stats.
 */
public interface Stats {

    /**
     * Invoke on start of method.
     *
     * @return start time of the method
     */
    long onStart();

    /**
     * Invoke on throwable of method.
     *
     * @param throwable exception thrown from the method
     */
    void onThrowable(Throwable throwable);

    /**
     * Invoke on finally of method.
     *
     * @param startTime start time of the method
     * @return time used for the method
     */
    long onFinally(long startTime);

    /**
     * Get the data of the stats.
     *
     * @return key-value formed data
     */
    Map<String, Object> harvest();

}
