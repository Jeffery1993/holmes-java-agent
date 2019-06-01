package com.jeffery.holmes.common.collector.aggregator;

import com.jeffery.holmes.common.util.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.logging.Logger;

/**
 * Abstract class for key-value stored aggregator.
 *
 * @param <K> type of key
 * @param <V> type of value
 * @see com.jeffery.holmes.common.collector.aggregator.AbstractAggregator
 */
public abstract class AbstractKeyValueAggregator<K, V> extends AbstractAggregator {

    protected Logger logger = LoggerFactory.getLogger(this.getClass());

    protected final ConcurrentMap<K, V> keyValue = new ConcurrentHashMap<K, V>();

    /**
     * Get value by key.
     *
     * <p>If the value associated with the key does not exist, a new value will be created.
     * If the size is greater than limit, the aggregator will be disabled and null will be returned.</p>
     *
     * @param key the key provided
     * @return value corresponding to the key
     */
    protected V getValue(K key) {
        V value = keyValue.get(key);
        if (value != null) {
            return value;
        }

        if (keyValue.size() >= getLimit()) {
            enabled = false;
            return null;
        }

        V newValue = createInstance();
        V res = keyValue.putIfAbsent(key, newValue);
        if (res == null) {
            return newValue;
        } else {
            return res;
        }
    }

    private V createInstance() {
        try {
            return getValueType().newInstance();
        } catch (Exception e) {
            logger.severe("Failed to create instance " + e.getMessage());
            return null;
        }
    }

    /**
     * Get size of the aggregator.
     *
     * @return size
     */
    public int size() {
        return keyValue.size();
    }

    /**
     * Clear data of the aggregator.
     */
    public void clear() {
        keyValue.clear();
    }

    @Override
    public List<Map<String, Object>> harvest() {
        List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
        for (Map.Entry<K, V> entry : keyValue.entrySet()) {
            list.add(getRow(entry.getKey(), entry.getValue()));
        }
        return list;
    }

    /**
     * Limit of keys in the aggregator. Can be overrided.
     *
     * @return limit of key number
     */
    protected int getLimit() {
        return 500;
    }

    /**
     * Make one row with key and value.
     *
     * @param key   key provided
     * @param value value
     * @return one row data
     */
    protected abstract Map<String, Object> getRow(K key, V value);

    /**
     * Get names of key.
     *
     * @return names of key
     */
    protected abstract String[] getKeyNames();

    /**
     * Get type of value.
     *
     * @return type of value
     */
    protected abstract Class<V> getValueType();

}
