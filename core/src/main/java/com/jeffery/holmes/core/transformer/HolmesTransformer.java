package com.jeffery.holmes.core.transformer;

import com.jeffery.holmes.common.trace.SpanEvent;
import com.jeffery.holmes.common.trace.TraceCollector;

import java.security.ProtectionDomain;

/**
 * Interface for holmes transformer.
 */
public interface HolmesTransformer {

    String TRACE_COLLECTOR_CLASS_NAME = TraceCollector.class.getName();
    String SPAN_EVENT_CLASS_NAME = SpanEvent.class.getName();
    String COLLECTOR_INSTANCE_SUFFIX = ".getInstance()";

    /**
     * Returns whether this transformer matches the className provided.
     *
     * @param className class name provided
     * @return true if match successfully
     */
    boolean matches(String className);

    /**
     * Try to transform the classfile byte array. May fail and throw any kind of exception.
     *
     * @param loader              classloader of the class
     * @param className           class name being redefined
     * @param classBeingRedefined {@link Class} being redefined
     * @param protectionDomain    protection domain of the class
     * @param classfileBuffer     classfile byte array
     * @return transformed classfile byte array if redefine successfully or the original one if failed
     * @throws Exception
     */
    byte[] transform(ClassLoader loader,
                     String className,
                     Class<?> classBeingRedefined,
                     ProtectionDomain protectionDomain,
                     byte[] classfileBuffer)
            throws Exception;

}
