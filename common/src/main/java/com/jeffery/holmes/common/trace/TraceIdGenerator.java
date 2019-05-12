package com.jeffery.holmes.common.trace;

import com.jeffery.holmes.common.util.ConfigManager;

import java.util.concurrent.atomic.AtomicLong;

public class TraceIdGenerator {

    private static final String TRACE_ID_PREFIX = ConfigManager.getClusterId() + " - " + ConfigManager.getAppId() + " - ";
    private static final AtomicLong COUNT = new AtomicLong(0L);

    public static String generate() {
        return TRACE_ID_PREFIX + COUNT.incrementAndGet();
    }

}
