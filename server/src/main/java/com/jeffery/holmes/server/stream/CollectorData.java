package com.jeffery.holmes.server.stream;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

public class CollectorData extends JSONObject implements Comparable<CollectorData> {

    public String getName() {
        return this.getString("name");
    }

    public Long getTimestamp() {
        return this.getLong("timestamp");
    }

    public AggregatorData getAggregator(String aggregator) {
        return JSON.parseObject(this.getString(aggregator), AggregatorData.class);
    }

    @Override
    public int compareTo(CollectorData o) {
        return Long.compare(this.getTimestamp(), o.getTimestamp());
    }

}
