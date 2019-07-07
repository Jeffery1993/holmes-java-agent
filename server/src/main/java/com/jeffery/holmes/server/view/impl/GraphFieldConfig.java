package com.jeffery.holmes.server.view.impl;

import com.alibaba.fastjson.JSON;
import com.jeffery.holmes.server.view.FieldConfig;
import org.springframework.util.StringUtils;
import org.w3c.dom.Element;

public class GraphFieldConfig implements FieldConfig {

    private String name;
    private String as;

    private GraphFieldConfig() {
    }

    public String getName() {
        return name;
    }

    public String getAs() {
        return as;
    }

    @Override
    public String toString() {
        return JSON.toJSONString(this);
    }

    public static FieldConfig valueOf(Element field) {
        GraphFieldConfig graphFieldConfig = new GraphFieldConfig();
        graphFieldConfig.name = field.getAttribute("name");
        if (StringUtils.isEmpty(graphFieldConfig.name)) {
            throw new IllegalArgumentException("Field name must not be empty!");
        }
        graphFieldConfig.as = field.getAttribute("as");
        if (StringUtils.isEmpty(graphFieldConfig.as)) {
            graphFieldConfig.as = graphFieldConfig.name;
        }
        return graphFieldConfig;
    }

}
