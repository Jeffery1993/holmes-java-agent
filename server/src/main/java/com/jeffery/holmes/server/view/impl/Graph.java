package com.jeffery.holmes.server.view.impl;

import com.alibaba.fastjson.JSON;
import com.jeffery.holmes.server.view.View;

import java.util.ArrayList;
import java.util.List;

/**
 * A graph is usually a representation of data in time series.
 */
public class Graph implements View {

    private final String title;
    private List<String> timeSeries;
    private List<Series> dataSeries;

    public Graph(String title) {
        this.title = title;
    }

    @Override
    public String getTitle() {
        return title;
    }

    @Override
    public Type getType() {
        return Type.graph;
    }

    public List<String> getTimeSeries() {
        return timeSeries;
    }

    public void setTimeSeries(List<String> timeSeries) {
        this.timeSeries = timeSeries;
    }

    public List<Series> getDataSeries() {
        return dataSeries;
    }

    public void setDataSeries(List<Series> dataSeries) {
        this.dataSeries = dataSeries;
    }

    public void addDataSeries(String name, List<Object> dataSeries) {
        if (this.dataSeries == null) {
            this.dataSeries = new ArrayList<>();
        }
        this.dataSeries.add(new Series(name, dataSeries));
    }

    @Override
    public String toString() {
        return JSON.toJSONString(this);
    }

    public static class Series {

        private final String name;
        private final String type = "line";
        private final List<Object> data;

        public Series(String name, List<Object> data) {
            this.name = name;
            this.data = data;
        }

        public String getName() {
            return name;
        }

        public String getType() {
            return type;
        }

        public List<Object> getData() {
            return data;
        }

        @Override
        public String toString() {
            return JSON.toJSONString(this);
        }

    }

}
