package com.jeffery.holmes.common.trace;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

/**
 * Collector for trace.
 */
public class TraceCollector {

    private static final ThreadLocal<SpanEvent> SPAN_EVENT_THREAD_LOCAL = new ThreadLocal<SpanEvent>();

    private static final int TRACE_QUEUE_SIZE = 1024;
    public static final BlockingQueue<Object> TRACE_BLOCKING_QUEUE = new ArrayBlockingQueue<Object>(TRACE_QUEUE_SIZE);

    /**
     * Invoke on start of the first method of a span.
     *
     * @param className  name of class
     * @param methodName name of method
     * @param eventType  type of span event
     * @param argument   argument of span event
     * @param traceId    uuid for trace
     * @param spanId     uuid for span
     * @param url        url of span
     * @param method     method of span
     * @return span event
     */
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

    /**
     * Invoke on start of the method, except for the first one of the span.
     *
     * @param className  name of class
     * @param methodName name of method
     * @param eventType  type of span event
     * @param argument   argument of span event
     * @return span event
     */
    public static SpanEvent start(String className, String methodName, String eventType, String argument) {
        SpanEvent spanEvent = SPAN_EVENT_THREAD_LOCAL.get();
        if (spanEvent != null) {
            SpanEvent newSpanEvent = new SpanEvent(spanEvent, className, methodName, eventType, argument);
            SPAN_EVENT_THREAD_LOCAL.set(newSpanEvent);
            return newSpanEvent;
        }
        return null;
    }

    /**
     * Invoke on throwable of the method.
     *
     * @param throwable exception thrown from the method
     */
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

    /**
     * Invoke on end of the method.
     *
     * @param code response code of the method
     */
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
