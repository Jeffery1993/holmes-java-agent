package com.jeffery.holmes.common.plugin.mysql;

import com.jeffery.holmes.common.collector.AbstractCollector;
import com.jeffery.holmes.common.collector.aggregator.PrimaryKey;
import com.jeffery.holmes.common.plugin.common.DefaultExceptionAggregator;
import com.jeffery.holmes.common.plugin.common.DefaultVersionAggregator;

import java.security.CodeSource;

public class MysqlCollector extends AbstractCollector {

    private MysqlAggregator mysqlAggregator = new MysqlAggregator();
    private DefaultExceptionAggregator exceptionAggregator = new DefaultExceptionAggregator();
    private DefaultVersionAggregator versionAggregator = new DefaultVersionAggregator();

    private MysqlCollector() {
        this.add(mysqlAggregator);
        this.add(exceptionAggregator);
        this.add(versionAggregator);
    }

    private static final MysqlCollector INSTANCE = new MysqlCollector();

    public static MysqlCollector getInstance() {
        return INSTANCE;
    }

    public void setVersion(CodeSource codeSource) {
        versionAggregator.setVersion(codeSource);
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
        exceptionAggregator.onThrowable(throwable);
    }

    public void onFinally() {
        if (!enabled) {
            return;
        }
        mysqlAggregator.onFinally();
    }

}
