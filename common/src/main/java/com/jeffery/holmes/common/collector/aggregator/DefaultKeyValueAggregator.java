package com.jeffery.holmes.common.collector.aggregator;

import com.jeffery.holmes.common.collector.stats.Stats;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Default implementation of {@link AbstractKeyValueAggregator}
 *
 * @param <K> type of key, extends {@link PrimaryKey}
 * @param <V> type of value, extends {@link Stats}
 * @see com.jeffery.holmes.common.collector.aggregator.AbstractKeyValueAggregator
 */
public abstract class DefaultKeyValueAggregator<K extends PrimaryKey, V extends Stats> extends AbstractKeyValueAggregator<K, V> {

    private ThreadLocal<V> statsLocal = new ThreadLocal<V>();
    private ThreadLocal<Long> timeLocal = new ThreadLocal<Long>();

    /**
     * Invoke on start of method.
     *
     * @param key key of aggregator
     */
    public void onStart(K key) {
        if (!enabled || key == null || key.size() == 0) {
            return;
        }
        V stats = getValue(key);
        if (stats != null) {
            long startTime = stats.onStart();
            statsLocal.set(stats);
            timeLocal.set(startTime);
        }
    }

    /**
     * Invoke on throwable of method.
     *
     * @param throwable exception
     */
    public void onThrowable(Throwable throwable) {
        if (!enabled) {
            return;
        }
        V stats = statsLocal.get();
        if (stats != null) {
            stats.onThrowable(throwable);
        }
    }

    /**
     * Invoke on finally of method.
     */
    public void onFinally() {
        if (!enabled) {
            return;
        }
        V stats = statsLocal.get();
        Long time = timeLocal.get();
        if (stats != null && time != null) {
            stats.onFinally(time);
            statsLocal.set(null);
            timeLocal.set(null);
        }
    }

    protected Map<String, Object> getRow(K key, V value) {
        Map<String, Object> row = new LinkedHashMap<String, Object>();
        String[] keyNames = getKeyNames();
        for (int i = 0; i < Math.min(keyNames.length, key.size()); i++) {
            row.put(keyNames[i], key.get(i));
        }
        row.putAll(value.harvest());
        return row;
    }

}
