package com.jeffery.holmes.core.transformer;

import com.jeffery.holmes.common.trace.SpanEvent;
import com.jeffery.holmes.common.trace.TraceCollector;

import java.security.ProtectionDomain;

public interface HolmesTransformer {

    String TRACE_COLLECTOR_CLASS_NAME = TraceCollector.class.getName();
    String SPAN_EVENT_CLASS_NAME = SpanEvent.class.getName();
    String COLLECTOR_INSTANCE_SUFFIX = ".getInstance()";

    boolean matches(String className);

    byte[] transform(ClassLoader loader,
                     String className,
                     Class<?> classBeingRedefined,
                     ProtectionDomain protectionDomain,
                     byte[] classfileBuffer)
            throws Exception;

}
