package com.jeffery.holmes.server.view.impl;

import com.alibaba.fastjson.JSON;
import com.jeffery.holmes.server.view.View;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * A graph is usually a representation of data in matrix format.
 */
public class Table implements View {

    private final String title;
    private List<Column> columns;
    private List<Map<String, Object>> data;

    public Table(String title) {
        this.title = title;
    }

    @Override
    public String getTitle() {
        return title;
    }

    @Override
    public Type getType() {
        return Type.table;
    }

    public List<Column> getColumns() {
        return columns;
    }

    public void setColumns(List<Column> columns) {
        this.columns = columns;
    }

    public void addColumn(Column column) {
        if (this.columns == null) {
            this.columns = new ArrayList<>();
        }
        this.columns.add(column);
    }

    public List<Map<String, Object>> getData() {
        return data;
    }

    public void setData(List<Map<String, Object>> data) {
        this.data = data;
    }

    @Override
    public String toString() {
        return JSON.toJSONString(this);
    }

    public static class Column {

        private final String prop;
        private final String label;

        public Column(String prop, String label) {
            this.prop = prop;
            this.label = label;
        }

        public String getProp() {
            return prop;
        }

        public String getLabel() {
            return label;
        }

        @Override
        public String toString() {
            return JSON.toJSONString(this);
        }

    }

}
