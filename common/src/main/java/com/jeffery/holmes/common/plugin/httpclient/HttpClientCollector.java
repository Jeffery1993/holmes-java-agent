package com.jeffery.holmes.common.plugin.httpclient;

import com.jeffery.holmes.common.collector.AbstractCollector;
import com.jeffery.holmes.common.collector.Collector;
import com.jeffery.holmes.common.collector.aggregator.PrimaryKey;

public class HttpClientCollector extends AbstractCollector {

    private HttpClientAggregator httpClientAggregator = new HttpClientAggregator();

    private HttpClientCollector() {
        this.add(httpClientAggregator);
    }

    public static Collector getInstance() {
        return new HttpClientCollector();
    }

    public void onStart(String url, String method) {
        if (!enabled || url == null || method == null) {
            return;
        }
        httpClientAggregator.onStart(new PrimaryKey(url, method));
    }

    public void onThrowable(Throwable throwable) {
        if (!enabled) {
            return;
        }
        httpClientAggregator.onThrowable(throwable);
    }

    public void onFinally() {
        if (!enabled) {
            return;
        }
        httpClientAggregator.onFinally();
    }

}
