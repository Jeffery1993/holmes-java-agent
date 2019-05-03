package com.jeffery.holmes.common.trace;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

public class TraceCollector {

    private static final ThreadLocal<SpanEvent> SPAN_EVENT_THREAD_LOCAL = new ThreadLocal<SpanEvent>();

    private static final int TRACE_QUEUE_SIZE = 1024;
    public static final BlockingQueue<Object> TRACE_BLOCKING_QUEUE = new ArrayBlockingQueue<Object>(TRACE_QUEUE_SIZE);

    public static void start(String className, String methodName, String eventType, String traceId, String spanId, String spanEventId, String url, String method) {
        SpanEvent spanEvent = SPAN_EVENT_THREAD_LOCAL.get();
        if (spanEvent == null) {
            // 创建Span
            Span span = new Span(traceId, spanId, url, method);
            SpanEvent newSpanEvent = new SpanEvent(className, methodName, eventType);
            newSpanEvent.setTraceId(span.getTraceId());
            newSpanEvent.setSpanId(span.getSpanId());
            newSpanEvent.setSpan(span);
            SPAN_EVENT_THREAD_LOCAL.set(newSpanEvent);
        } else {
            Span span = spanEvent.getSpan();
            SpanEvent newSpanEvent = new SpanEvent();
            newSpanEvent.setSpan(span);
            newSpanEvent.setParent(spanEvent);
            SPAN_EVENT_THREAD_LOCAL.set(newSpanEvent);
        }
    }

    public static void exception(Throwable throwable) {
        SpanEvent spanEvent = SPAN_EVENT_THREAD_LOCAL.get();
        if (spanEvent != null) {
            spanEvent.setError(true);
            spanEvent.setErrorType(throwable.getClass().getName());
            spanEvent.setErrorMessage(throwable.getMessage());
            Span span = spanEvent.getSpan();
            span.setError(true);
            span.setErrorType(throwable.getClass().getName());
            span.setErrorMessage(throwable.getMessage());
        }
    }

    public static void end(int code, boolean hasNextSpan) {
        SpanEvent spanEvent = SPAN_EVENT_THREAD_LOCAL.get();
        if (spanEvent != null) {
            Span span = spanEvent.getSpan();
            long usedTime = System.currentTimeMillis() - spanEvent.getStartTime();
            if (hasNextSpan) {
                spanEvent.setNextSpanId(span.getNextSpanId());
            }
            spanEvent.setUsedTime(usedTime);
            if (spanEvent.getParent() == null) {
                span.setUsedTime(usedTime);
                span.setCode(code);
                SPAN_EVENT_THREAD_LOCAL.set(null);
                TRACE_BLOCKING_QUEUE.offer(span);
            } else {
                SPAN_EVENT_THREAD_LOCAL.set(spanEvent.getParent());
            }
            TRACE_BLOCKING_QUEUE.offer(spanEvent);
        }
    }

}
