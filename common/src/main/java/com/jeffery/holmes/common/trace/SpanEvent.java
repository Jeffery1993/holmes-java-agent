package com.jeffery.holmes.common.trace;

import java.util.concurrent.atomic.AtomicInteger;

public class SpanEvent extends SpanEventData {

    private Span span;
    private SpanEvent parent;

    private AtomicInteger count = new AtomicInteger(0);

    public String generateNextSpanEventId() {
        String spanEventId = (this.getSpanEventId() == null) ? "1" : this.getSpanEventId();
        return new StringBuilder().append(spanEventId).append("-").append(count.incrementAndGet()).toString();
    }

}
