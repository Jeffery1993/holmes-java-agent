package com.jeffery.holmes.common.plugin.url;

import com.jeffery.holmes.common.collector.AbstractCollector;
import com.jeffery.holmes.common.collector.Collector;
import com.jeffery.holmes.common.collector.aggregator.PrimaryKey;

public class UrlCollector extends AbstractCollector {

    private UrlStatsAggregator urlStatsAggregator = new UrlStatsAggregator();

    private UrlCollector() {
        this.add(urlStatsAggregator);
    }

    public static Collector getInstance() {
        return new UrlCollector();
    }

    public void onStart(String url, String method) {
        if (!enabled || url == null || method == null) {
            return;
        }
        urlStatsAggregator.onStart(new PrimaryKey(url, method));
    }

    public void onThrowable(Throwable throwable) {
        if (!enabled) {
            return;
        }
        urlStatsAggregator.onThrowable(throwable);
    }

    public void onFinally() {
        if (!enabled) {
            return;
        }
        urlStatsAggregator.onFinally();
    }

}
