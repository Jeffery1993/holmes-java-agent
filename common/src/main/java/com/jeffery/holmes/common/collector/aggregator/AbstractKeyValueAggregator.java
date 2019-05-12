package com.jeffery.holmes.common.collector.aggregator;

import com.jeffery.holmes.common.util.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.logging.Logger;

public abstract class AbstractKeyValueAggregator<K, V> extends AbstractAggregator {

    protected Logger logger = LoggerFactory.getLogger(this.getClass());

    protected final ConcurrentMap<K, V> keyValue = new ConcurrentHashMap<K, V>();

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

    public int size() {
        return keyValue.size();
    }

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

    protected int getLimit() {
        return 500;
    }

    protected abstract Map<String, Object> getRow(K key, V value);

    protected abstract String[] getKeyNames();

    protected abstract Class<V> getValueType();

}
