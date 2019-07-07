package com.jeffery.holmes.server.view.impl;

import com.alibaba.fastjson.JSON;
import com.jeffery.holmes.server.stream.CollectorData;
import com.jeffery.holmes.server.util.DateUtils;
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

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class GraphConfig implements ViewConfig {

    private static final Logger LOGGER = LoggerFactory.getLogger(GraphConfig.class);

    private String aggregator;
    private String title;
    private List<FieldConfig> fieldConfigs;

    private GraphConfig() {
    }

    @Override
    public String getAggregator() {
        return aggregator;
    }

    @Override
    public String getTitle() {
        return title;
    }

    @Override
    public List<FieldConfig> getFieldConfigs() {
        return fieldConfigs;
    }

    @Override
    public View compute(List<CollectorData> collectorDataList) {
        Graph graph = new Graph(this.getTitle());
        Map<String, String> nameAs = this.getFieldConfigs().stream()
                .map(m -> (GraphFieldConfig) m)
                .collect(Collectors.toMap(GraphFieldConfig::getName, GraphFieldConfig::getAs));
        List<String> timeSeries = collectorDataList.stream()
                .map(c -> DateUtils.format(c.getTimestamp()))
                .collect(Collectors.toList());
        graph.setTimeSeries(timeSeries);
        for (FieldConfig fieldConfig : this.getFieldConfigs()) {
            GraphFieldConfig graphFieldConfig = (GraphFieldConfig) fieldConfig;
            List<Object> dataSeries = collectorDataList.stream()
                    .map(c -> c.getAggregator(this.getAggregator()).get(0).get(graphFieldConfig.getName()))
                    .collect(Collectors.toList());
            graph.addDataSeries(graphFieldConfig.getAs(), dataSeries);
        }
        return graph;
    }

    @Override
    public String toString() {
        return JSON.toJSONString(this);
    }

    public static ViewConfig valueOf(Element view) {
        GraphConfig graphConfig = new GraphConfig();
        graphConfig.aggregator = view.getAttribute("aggregator");
        if (StringUtils.isEmpty(graphConfig.aggregator)) {
            throw new IllegalArgumentException("Aggregator cannot be empty!");
        }
        graphConfig.title = view.getAttribute("title");
        if (StringUtils.isEmpty(graphConfig.title)) {
            graphConfig.title = graphConfig.aggregator;
        }
        graphConfig.fieldConfigs = parseFields(view.getChildNodes());
        if (CollectionUtils.isEmpty(graphConfig.fieldConfigs)) {
            throw new IllegalArgumentException("Field configs cannot be empty!");
        }
        return graphConfig;
    }

    private static List<FieldConfig> parseFields(NodeList fieldNodeList) {
        List<FieldConfig> fieldConfigs = new ArrayList<>();
        for (int i = 0; i < fieldNodeList.getLength(); i++) {
            Node fieldNode = fieldNodeList.item(i);
            if (fieldNode instanceof Element) {
                Element field = (Element) fieldNode;
                try {
                    fieldConfigs.add(GraphFieldConfig.valueOf(field));
                } catch (Exception e) {
                    LOGGER.warn("Failed to parse field config: " + e.getMessage());
                }
            }
        }
        return fieldConfigs;
    }

}
