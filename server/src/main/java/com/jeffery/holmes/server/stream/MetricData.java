package com.jeffery.holmes.server.stream;

import com.jeffery.holmes.server.view.impl.TableConfig;

import java.util.HashMap;

/**
 * Metric data.
 *
 * <p>Consists of a map of metrics.</p>
 */
public class MetricData extends HashMap<String, Object> {

    public TableConfig.Tuple primaryKey(TableConfig.Tuple groupBy) {
        int size = groupBy.getValues().length;
        String[] primaryKey = new String[size];
        for (int i = 0; i < size; i++) {
            primaryKey[i] = this.get(groupBy.getValues()[i]).toString();
        }
        return new TableConfig.Tuple(primaryKey);
    }

}
