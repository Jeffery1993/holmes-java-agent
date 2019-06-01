package com.jeffery.holmes.common.trace;

import java.util.HashMap;
import java.util.Map;

/**
 * Data of {@link SpanEvent}
 *
 * @see com.jeffery.holmes.common.trace.SpanEvent
 */
public class SpanEventData {

    private String traceId;
    private String spanId;
    private String nextSpanId;
    private String spanEventId;

    private long startTime;
    private long usedTime;

    private String className;
    private String methodName;
    private String eventType;
    private String argument;

    private boolean error;
    private String errorType;
    private String errorMessage;

    private Map<String, Object> parameters;

    public String getTraceId() {
        return traceId;
    }

    public void setTraceId(String traceId) {
        this.traceId = traceId;
    }

    public String getSpanId() {
        return spanId;
    }

    public void setSpanId(String spanId) {
        this.spanId = spanId;
    }

    public String getNextSpanId() {
        return nextSpanId;
    }

    public void setNextSpanId(String nextSpanId) {
        this.nextSpanId = nextSpanId;
    }

    public String getSpanEventId() {
        return spanEventId;
    }

    public void setSpanEventId(String spanEventId) {
        this.spanEventId = spanEventId;
    }

    public long getStartTime() {
        return startTime;
    }

    public void setStartTime(long startTime) {
        this.startTime = startTime;
    }

    public long getUsedTime() {
        return usedTime;
    }

    public void setUsedTime(long usedTime) {
        this.usedTime = usedTime;
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public String getMethodName() {
        return methodName;
    }

    public void setMethodName(String methodName) {
        this.methodName = methodName;
    }

    public String getEventType() {
        return eventType;
    }

    public void setEventType(String eventType) {
        this.eventType = eventType;
    }

    public String getArgument() {
        return argument;
    }

    public void setArgument(String argument) {
        this.argument = argument;
    }

    public boolean isError() {
        return error;
    }

    public void setError(boolean error) {
        this.error = error;
    }

    public String getErrorType() {
        return errorType;
    }

    public void setErrorType(String errorType) {
        this.errorType = errorType;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    public Map<String, Object> getParameters() {
        return parameters;
    }

    public void setParameters(Map<String, Object> parameters) {
        this.parameters = parameters;
    }

    public void addParameter(String key, Object value) {
        if (this.parameters == null) {
            this.parameters = new HashMap<String, Object>();
        }
        this.parameters.put(key, value);
    }

}
