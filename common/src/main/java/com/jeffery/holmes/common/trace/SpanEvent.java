package com.jeffery.holmes.common.trace;

import java.util.concurrent.atomic.AtomicInteger;

public class SpanEvent extends SpanEventData {

    private Span span;
    private SpanEvent parent;

    private AtomicInteger count = new AtomicInteger(0);

    public SpanEvent() {
        this.setStartTime(System.currentTimeMillis());
    }

    public SpanEvent(String className, String methodName, String eventType) {
        this.setClassName(className);
        this.setMethodName(methodName);
        this.setEventType(eventType);
        this.setStartTime(System.currentTimeMillis());
    }

    public Span getSpan() {
        return span;
    }

    public void setSpan(Span span) {
        this.span = span;
    }

    public SpanEvent getParent() {
        return parent;
    }

    public void setParent(SpanEvent parent) {
        this.parent = parent;
    }

    public String generateNextSpanEventId() {
        String spanEventId = (this.getSpanEventId() == null) ? "1" : this.getSpanEventId();
        return new StringBuilder().append(spanEventId).append("-").append(count.incrementAndGet()).toString();
    }

}
