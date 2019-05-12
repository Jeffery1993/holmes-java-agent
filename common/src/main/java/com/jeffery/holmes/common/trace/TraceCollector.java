package com.jeffery.holmes.common.trace;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

public class TraceCollector {

    private static final ThreadLocal<SpanEvent> SPAN_EVENT_THREAD_LOCAL = new ThreadLocal<SpanEvent>();

    private static final int TRACE_QUEUE_SIZE = 1024;
    public static final BlockingQueue<Object> TRACE_BLOCKING_QUEUE = new ArrayBlockingQueue<Object>(TRACE_QUEUE_SIZE);

    public static SpanEvent start(String className, String methodName, String eventType, String argument, String traceId, String spanId, String url, String method) {
        SpanEvent spanEvent = SPAN_EVENT_THREAD_LOCAL.get();
        if (spanEvent == null) {
            Span span = new Span(traceId, spanId, url, method);
            SpanEvent newSpanEvent = new SpanEvent(span, className, methodName, eventType, argument);
            SPAN_EVENT_THREAD_LOCAL.set(newSpanEvent);
            return newSpanEvent;
        }
        return null;
    }

    public static SpanEvent start(String className, String methodName, String eventType, String argument) {
        SpanEvent spanEvent = SPAN_EVENT_THREAD_LOCAL.get();
        if (spanEvent != null) {
            SpanEvent newSpanEvent = new SpanEvent(spanEvent, className, methodName, eventType, argument);
            SPAN_EVENT_THREAD_LOCAL.set(newSpanEvent);
            return newSpanEvent;
        }
        return null;
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

    public static void end(int code) {
        SpanEvent spanEvent = SPAN_EVENT_THREAD_LOCAL.get();
        if (spanEvent != null) {
            Span span = spanEvent.getSpan();
            long usedTime = System.currentTimeMillis() - spanEvent.getStartTime();
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
