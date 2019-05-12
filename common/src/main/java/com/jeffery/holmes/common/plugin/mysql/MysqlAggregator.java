package com.jeffery.holmes.common.plugin.mysql;

import com.jeffery.holmes.common.collector.aggregator.DefaultKeyValueAggregator;
import com.jeffery.holmes.common.collector.aggregator.PrimaryKey;
import com.jeffery.holmes.common.collector.stats.SectionStats;

public class MysqlAggregator extends DefaultKeyValueAggregator<PrimaryKey, SectionStats> {

    @Override
    public String getName() {
        return "mysql";
    }

    @Override
    protected String[] getKeyNames() {
        return new String[]{"sql"};
    }

    @Override
    protected Class<SectionStats> getValueType() {
        return SectionStats.class;
    }

}
