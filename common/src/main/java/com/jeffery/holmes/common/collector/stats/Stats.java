package com.jeffery.holmes.common.collector.stats;

import java.util.Map;

public interface Stats {

    long onStart();

    void onThrowable(Throwable throwable);

    long onFinally(long startTime);

    Map<String, Object> harvest();

}
