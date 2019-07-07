package com.jeffery.holmes.common.trace;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * A span event represents a event in the {@link Span}.
 *
 * @see com.jeffery.holmes.common.trace.Span
 * @see com.jeffery.holmes.common.trace.SpanEventData
 */
public class SpanEvent extends SpanEventData {

    private Span span;
    private SpanEvent parent;

    private AtomicInteger count = new AtomicInteger(0);

    public SpanEvent(Span span, String className, String methodName, String eventType, String argument) {
        this.setTraceId(span.getTraceId());
        this.setSpanId(span.getSpanId());
        this.setSpanEventId("1");
        this.setSpan(span);
        this.setClassName(className);
        this.setMethodName(methodName);
        this.setEventType(eventType);
        this.setArgument(argument);
        this.setStartTime(System.currentTimeMillis());
    }

    public SpanEvent(SpanEvent spanEvent, String className, String methodName, String eventType, String argument) {
        this(spanEvent.takeSpan(), className, methodName, eventType, argument);
        this.setParent(spanEvent);
        this.setSpanEventId(spanEvent.generateNextSpanEventId());
    }

    /**
     * Equivalent to get span. Rename to avoid serialization.
     *
     * @return span
     */
    public Span takeSpan() {
        return span;
    }

    public void setSpan(Span span) {
        this.span = span;
    }

    /**
     * Equivalent to get parent. Rename to avoid serialization.
     *
     * @return parent span event
     */
    public SpanEvent takeParent() {
        return parent;
    }

    public void setParent(SpanEvent parent) {
        this.parent = parent;
    }

    public String attachNextSpanId() {
        String nextSpanId = this.takeSpan().generateNextSpanId();
        this.setNextSpanId(nextSpanId);
        return nextSpanId;
    }

    public String generateNextSpanEventId() {
        String spanEventId = (this.getSpanEventId() == null) ? "1" : this.getSpanEventId();
        return new StringBuilder().append(spanEventId).append("-").append(count.incrementAndGet()).toString();
    }

}
