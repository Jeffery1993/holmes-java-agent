package com.jeffery.holmes.server.view;

import com.jeffery.holmes.server.stream.CollectorData;
import com.jeffery.holmes.server.view.impl.GraphConfig;
import com.jeffery.holmes.server.view.impl.TableConfig;
import org.w3c.dom.Element;

import java.util.List;

/**
 * Interface for view configuration.
 */
public interface ViewConfig {

    String getAggregator();

    String getTitle();

    List<FieldConfig> getFieldConfigs();

    View compute(List<CollectorData> collectorDataList);

    public static ViewConfig parse(Element view) {
        ViewCategory category = ViewCategory.valueOf(view.getNodeName());
        if (ViewCategory.table == category) {
            return TableConfig.valueOf(view);
        } else if (ViewCategory.graph == category) {
            return GraphConfig.valueOf(view);
        } else {
            return null;
        }
    }

    public enum ViewCategory {
        graph, table;
    }

}
