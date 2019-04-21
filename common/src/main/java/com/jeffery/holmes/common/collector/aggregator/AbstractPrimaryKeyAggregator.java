package com.jeffery.holmes.common.collector.aggregator;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

public abstract class AbstractPrimaryKeyAggregator<K, V> extends AbstractAggregator {

    protected volatile ConcurrentMap<K, V> values = new ConcurrentHashMap<K, V>();

    public V get(K key) {
        V value = values.get(key);
        if (value == null) {
            try {
                value = getValueType().newInstance();
            } catch (Exception e) {
                return null;
            }
            values.putIfAbsent(key, value);
        }
        return value;
    }

    public V remove(K key) {
        return values.remove(key);
    }

    public int size() {
        return values.size();
    }

    public void clear() {
        values.clear();
    }

    @Override
    public List<Map<String, Object>> harvest() {
        List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
        for (Map.Entry<K, V> entry : values.entrySet()) {
            Map<String, Object> row = constructOneRow(entry.getKey(), entry.getValue());
            list.add(row);
        }
        return list;
    }

    protected abstract Class<V> getValueType();

    protected abstract Map<String, Object> constructOneRow(K key, V value);

}
