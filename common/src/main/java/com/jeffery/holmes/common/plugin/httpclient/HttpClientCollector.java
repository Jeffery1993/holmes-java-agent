package com.jeffery.holmes.common.plugin.httpclient;

import com.jeffery.holmes.common.collector.AbstractCollector;
import com.jeffery.holmes.common.collector.aggregator.PrimaryKey;
import com.jeffery.holmes.common.plugin.common.DefaultExceptionAggregator;
import com.jeffery.holmes.common.plugin.common.DefaultVersionAggregator;

import java.security.CodeSource;

public class HttpClientCollector extends AbstractCollector {

    private HttpClientAggregator httpClientAggregator = new HttpClientAggregator();
    private DefaultExceptionAggregator exceptionAggregator = new DefaultExceptionAggregator();
    private DefaultVersionAggregator versionAggregator = new DefaultVersionAggregator();

    private HttpClientCollector() {
        this.add(httpClientAggregator);
        this.add(exceptionAggregator);
        this.add(versionAggregator);
    }

    private static final HttpClientCollector INSTANCE = new HttpClientCollector();

    public static HttpClientCollector getInstance() {
        return INSTANCE;
    }

    public void setVersion(CodeSource codeSource) {
        versionAggregator.setVersion(codeSource);
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
        exceptionAggregator.onThrowable(throwable);
    }

    public void onFinally() {
        if (!enabled) {
            return;
        }
        httpClientAggregator.onFinally();
    }

}
