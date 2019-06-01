package com.jeffery.holmes.common.plugin.mysql;

import com.jeffery.holmes.common.collector.AbstractCollector;
import com.jeffery.holmes.common.collector.aggregator.PrimaryKey;

public class MysqlCollector extends AbstractCollector {

    private MysqlAggregator mysqlAggregator = new MysqlAggregator();

    private MysqlCollector() {
        this.add(mysqlAggregator);
    }

    private static final MysqlCollector INSTANCE = new MysqlCollector();

    public static MysqlCollector getInstance() {
        return INSTANCE;
    }

    public void onStart(String sql) {
        if (!enabled || sql == null) {
            return;
        }
        mysqlAggregator.onStart(new PrimaryKey(sql));
    }

    public void onThrowable(Throwable throwable) {
        if (!enabled) {
            return;
        }
        mysqlAggregator.onThrowable(throwable);
    }

    public void onFinally() {
        if (!enabled) {
            return;
        }
        mysqlAggregator.onFinally();
    }

}
