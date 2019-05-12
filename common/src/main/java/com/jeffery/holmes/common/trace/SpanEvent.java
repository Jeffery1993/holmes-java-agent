package com.jeffery.holmes.common.trace;

import com.jeffery.holmes.common.util.ConfigManager;

import java.util.concurrent.atomic.AtomicInteger;

public class SpanEvent extends SpanEventData {

    private Span span;
    private SpanEvent parent;

    private AtomicInteger count = new AtomicInteger(0);

    public SpanEvent(Span span, String className, String methodName, String eventType, String argument) {
        this.setTraceId(span.getTraceId());
        this.setSpanId(span.getSpanId());
        this.setSpanEventId("1");
        this.setSpan(span);
        this.setClusterId(ConfigManager.getClusterId());
        this.setAppId(ConfigManager.getAppId());
        this.setClassName(className);
        this.setMethodName(methodName);
        this.setEventType(eventType);
        this.setStartTime(System.currentTimeMillis());
    }

    public SpanEvent(SpanEvent spanEvent, String className, String methodName, String eventType, String argument) {
        this(spanEvent.getSpan(), className, methodName, eventType, argument);
        this.setParent(spanEvent);
        this.setSpanEventId(spanEvent.generateNextSpanEventId());
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

    public String attachNextSpanId() {
        String nextSpanId = this.getSpan().generateNextSpanId();
        this.setNextSpanId(nextSpanId);
        return nextSpanId;
    }

    public String generateNextSpanEventId() {
        String spanEventId = (this.getSpanEventId() == null) ? "1" : this.getSpanEventId();
        return new StringBuilder().append(spanEventId).append("-").append(count.incrementAndGet()).toString();
    }

}
