package com.jeffery.holmes.common.plugin.httpclient;

import com.jeffery.holmes.common.collector.aggregator.DefaultKeyValueAggregator;
import com.jeffery.holmes.common.collector.aggregator.PrimaryKey;
import com.jeffery.holmes.common.collector.stats.SectionStats;

public class HttpClientAggregator extends DefaultKeyValueAggregator<PrimaryKey, SectionStats> {

    @Override
    public String getName() {
        return "httpclient";
    }

    @Override
    protected String[] getKeyNames() {
        return new String[]{"url", "method"};
    }

    @Override
    protected Class<SectionStats> getValueType() {
        return SectionStats.class;
    }

}
