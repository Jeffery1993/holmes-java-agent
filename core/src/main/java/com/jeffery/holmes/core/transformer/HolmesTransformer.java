package com.jeffery.holmes.core.transformer;

import com.jeffery.holmes.common.trace.TraceCollector;

import java.security.ProtectionDomain;

public interface HolmesTransformer {

    String TRACE_COLLECTOR_NAME = TraceCollector.class.getName();

    boolean matches(String className);

    byte[] transform(ClassLoader loader,
                     String className,
                     Class<?> classBeingRedefined,
                     ProtectionDomain protectionDomain,
                     byte[] classfileBuffer)
            throws Exception;

}
