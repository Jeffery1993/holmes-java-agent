package com.jeffery.holmes.common.trace;

import java.util.concurrent.atomic.AtomicInteger;

public class Span extends SpanData {

    private AtomicInteger count = new AtomicInteger(0);

    public String generateNextSpanId() {
        String spanId = (this.getSpanId() == null) ? "1" : this.getSpanId();
        return new StringBuilder().append(spanId).append("-").append(count.incrementAndGet()).toString();
    }

}
