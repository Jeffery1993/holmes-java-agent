package com.jeffery.holmes.common.plugin.url;

import com.jeffery.holmes.common.collector.AbstractCollector;
import com.jeffery.holmes.common.collector.aggregator.PrimaryKey;

import java.util.ArrayList;
import java.util.List;

public class UrlCollector extends AbstractCollector {

    private UrlStatsAggregator urlStatsAggregator = new UrlStatsAggregator();

    private UrlCollector() {
        this.add(urlStatsAggregator);
    }

    private static final UrlCollector INSTANCE = new UrlCollector();

    public static UrlCollector getInstance() {
        return INSTANCE;
    }

    public boolean onFilter(String url) {
        return DefaultUrlFilter.onFilter(url);
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

    static class DefaultUrlFilter {

        private static final List<String> BLACK_LIST = new ArrayList<String>();

        static {
            BLACK_LIST.add(".js");
            BLACK_LIST.add(".css");
            BLACK_LIST.add(".png");
            BLACK_LIST.add(".jpg");
            BLACK_LIST.add(".jpeg");
        }

        static boolean onFilter(String url) {
            if (url == null) {
                return true;
            }
            for (String suffix : BLACK_LIST) {
                if (url.endsWith(suffix)) {
                    return true;
                }
            }
            return false;
        }

    }

}
