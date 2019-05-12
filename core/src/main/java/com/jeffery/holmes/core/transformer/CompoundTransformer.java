package com.jeffery.holmes.core.transformer;

import com.jeffery.holmes.common.util.LoggerFactory;

import java.lang.instrument.ClassFileTransformer;
import java.lang.instrument.IllegalClassFormatException;
import java.security.ProtectionDomain;
import java.util.logging.Logger;

public class CompoundTransformer implements ClassFileTransformer {

    private static final Logger LOGGER = LoggerFactory.getLogger(CompoundTransformer.class);

    @Override
    public byte[] transform(ClassLoader loader, String className, Class<?> classBeingRedefined, ProtectionDomain protectionDomain, byte[] classfileBuffer) throws IllegalClassFormatException {
        if (className.startsWith("sun.") || className.startsWith("java.")) {
            return classfileBuffer;
        } else {
            HolmesTransformer transformer = TransformerManager.getMatchedTransformer(className);
            if (transformer != null) {
                try {
                    LOGGER.info("Start to transform class: " + className);
                    classfileBuffer = transformer.transform(loader, className, classBeingRedefined, protectionDomain, classfileBuffer);
                    LOGGER.info("Succeed to transform class: " + className);
                } catch (Exception e) {
                    LOGGER.severe("Failed to transform class: " + className + " due to " + e);
                }
            }
            return classfileBuffer;
        }
    }

}
