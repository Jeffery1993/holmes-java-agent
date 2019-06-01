package com.jeffery.holmes.common.trace;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * A span represents an invocation from user or last node, may be HTTP or RPC.
 *
 * @see com.jeffery.holmes.common.trace.SpanData
 */
public class Span extends SpanData {

    private AtomicInteger count = new AtomicInteger(0);

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
