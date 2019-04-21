package com.jeffery.holmes.common.trace;

import java.util.concurrent.atomic.AtomicInteger;

public class Span extends SpanData {

    private AtomicInteger count = new AtomicInteger(0);

    public Span() {
        this.setStartTime(System.currentTimeMillis());
    }

    public Span(String traceId, String spanId, String url, String method) {
        this.setTraceId(traceId == null ? TraceIdGenerator.generate() : traceId);
        this.setSpanId(spanId == null ? "1" : spanId);
        this.setUrl(url);
        this.setMethod(method);
        this.setStartTime(System.currentTimeMillis());
    }

    public String generateNextSpanId() {
        String spanId = (this.getSpanId() == null) ? "1" : this.getSpanId();
        return new StringBuilder().append(spanId).append("-").append(count.incrementAndGet()).toString();
    }

}
