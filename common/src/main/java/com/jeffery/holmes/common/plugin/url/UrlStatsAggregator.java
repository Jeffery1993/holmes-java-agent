package com.jeffery.holmes.common.plugin.url;

import com.jeffery.holmes.common.collector.aggregator.DefaultKeyValueAggregator;
import com.jeffery.holmes.common.collector.aggregator.PrimaryKey;
import com.jeffery.holmes.common.collector.stats.SectionStats;

public class UrlStatsAggregator extends DefaultKeyValueAggregator<PrimaryKey, SectionStats> {

    @Override
    public String getName() {
        return "url";
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
