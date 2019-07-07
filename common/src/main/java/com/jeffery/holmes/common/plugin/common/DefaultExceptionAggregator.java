package com.jeffery.holmes.common.plugin.common;

import com.jeffery.holmes.common.collector.aggregator.DefaultKeyValueAggregator;
import com.jeffery.holmes.common.collector.aggregator.PrimaryKey;

public class DefaultExceptionAggregator extends DefaultKeyValueAggregator<PrimaryKey, ExceptionStats> {

    @Override
    public String getName() {
        return "exception";
    }

    @Override
    protected String[] getKeyNames() {
        return new String[]{"exception", "message"};
    }

    @Override
    protected Class<ExceptionStats> getValueType() {
        return ExceptionStats.class;
    }

    @Override
    public void onStart(PrimaryKey key) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void onThrowable(Throwable throwable) {
        if (!enabled || throwable == null) {
            return;
        }
        PrimaryKey primaryKey = new PrimaryKey(throwable.getClass().getName(), throwable.getMessage());
        ExceptionStats stats = getValue(primaryKey);
        if (stats != null) {
            stats.onThrowable(throwable);
        }
    }

    @Override
    public void onFinally() {
        throw new UnsupportedOperationException();
    }

}
