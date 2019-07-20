package com.jeffery.holmes.server.view.impl;

import com.alibaba.fastjson.JSON;
import com.jeffery.holmes.server.stream.CollectorData;
import com.jeffery.holmes.server.stream.MetricData;
import com.jeffery.holmes.server.view.FieldConfig;
import com.jeffery.holmes.server.view.View;
import com.jeffery.holmes.server.view.ViewConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Configuration for table.
 */
public class TableConfig implements ViewConfig {

    private static final Logger LOGGER = LoggerFactory.getLogger(TableConfig.class);

    private String aggregator;
    private String title;
    private Tuple groupBy;
    private List<FieldConfig> fieldConfigs;

    private TableConfig() {
    }

    @Override
    public String getAggregator() {
        return aggregator;
    }

    @Override
    public String getTitle() {
        return title;
    }

    public Tuple getGroupBy() {
        return groupBy;
    }

    @Override
    public List<FieldConfig> getFieldConfigs() {
        return fieldConfigs;
    }

    @Override
    public View compute(List<CollectorData> collectorDataList) {
        List<Map<String, Object>> computed = new ArrayList<>();

        // flat map to stream of metric data
        List<MetricData> metricDataList = collectorDataList.parallelStream()
                .map(c -> c.getAggregator(this.getAggregator()))
                .filter(agg -> agg != null)
                .flatMap(agg -> agg.stream())
                .collect(Collectors.toList());

        if (this.getGroupBy() != null) {
            // grouping by primary key
            Map<Tuple, List<MetricData>> grouped = metricDataList.parallelStream()
                    .collect(Collectors.groupingBy(m -> m.primaryKey(this.getGroupBy()), Collectors.toList()));

            // compute concurrently
            computed = grouped.entrySet().parallelStream()
                    .map(entry -> computeInternal(this.getGroupBy(), entry.getKey(), entry.getValue(), this.fieldConfigs))
                    .collect(Collectors.toList());
        } else {
            computed.add(computeInternal(null, null, metricDataList, this.fieldConfigs));
        }

        // encapsulated as a table
        Table table = new Table(this.getTitle());
        if (this.getGroupBy() != null) {
            for (String groupBy : this.getGroupBy().values) {
                table.addColumn(new Table.Column(groupBy, groupBy));
            }
        }
        for (FieldConfig fieldConfig : this.fieldConfigs) {
            TableFieldConfig tableFieldConfig = (TableFieldConfig) fieldConfig;
            table.addColumn(new Table.Column(tableFieldConfig.getFunction().toString(), tableFieldConfig.getAs()));
        }
        table.setData(computed);
        return table;
    }

    private Map<String, Object> computeInternal(Tuple groupBy, Tuple primaryKey, List<MetricData> metricDataList, List<FieldConfig> fieldConfigs) {
        Map<String, Object> res = new LinkedHashMap<>();
        if (groupBy != null && primaryKey != null) {
            for (int i = 0; i < Math.min(groupBy.values.length, primaryKey.values.length); i++) {
                res.put(groupBy.values[i], primaryKey.values[i]);
            }
        }
        for (FieldConfig fieldConfig : fieldConfigs) {
            TableFieldConfig tableFieldConfig = (TableFieldConfig) fieldConfig;
            List<Object> data = metricDataList.parallelStream()
                    .map(m -> m.get(tableFieldConfig.getFunction().getField()))
                    .filter(m -> m != null)
                    .collect(Collectors.toList());
            Object output = null;
            try {
                output = tableFieldConfig.getFunction().apply(data);
            } catch (Exception e) {
                LOGGER.warn("Failed to apply function [" + tableFieldConfig.getFunction() + "] to data " + data + ", " + e.getMessage());
            }
            res.put(tableFieldConfig.getFunction().toString(), output);
        }
        return res;
    }

    @Override
    public String toString() {
        return JSON.toJSONString(this);
    }

    public static ViewConfig valueOf(Element view) {
        TableConfig tableConfig = new TableConfig();
        tableConfig.aggregator = view.getAttribute("aggregator");
        if (StringUtils.isEmpty(tableConfig.aggregator)) {
            throw new IllegalArgumentException("Aggregator cannot be empty!");
        }
        tableConfig.title = view.getAttribute("title");
        if (StringUtils.isEmpty(tableConfig.title)) {
            tableConfig.title = tableConfig.aggregator;
        }
        if (!StringUtils.isEmpty(view.getAttribute("groupby"))) {
            tableConfig.groupBy = new Tuple(view.getAttribute("groupby").split(","));
        }
        tableConfig.fieldConfigs = parseFields(view.getChildNodes());
        if (CollectionUtils.isEmpty(tableConfig.fieldConfigs)) {
            throw new IllegalArgumentException("Field configs cannot be empty!");
        }
        return tableConfig;
    }

    private static List<FieldConfig> parseFields(NodeList fieldNodeList) {
        List<FieldConfig> fieldConfigs = new ArrayList<>();
        for (int i = 0; i < fieldNodeList.getLength(); i++) {
            Node fieldNode = fieldNodeList.item(i);
            if (fieldNode instanceof Element) {
                Element field = (Element) fieldNode;
                try {
                    fieldConfigs.add(TableFieldConfig.valueOf(field));
                } catch (Exception e) {
                    LOGGER.warn("Failed to parse field config: " + e.getMessage());
                }
            }
        }
        return fieldConfigs;
    }

    public static class Tuple {

        private final String[] values;

        public Tuple(String[] values) {
            this.values = values;
        }

        public String[] getValues() {
            return values;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            Tuple tuple = (Tuple) o;
            return Arrays.equals(values, tuple.values);
        }

        @Override
        public int hashCode() {
            return Arrays.hashCode(values);
        }

        @Override
        public String toString() {
            return Arrays.toString(values);
        }

    }

}
