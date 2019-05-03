package com.jeffery.holmes.core.transformer;

import com.jeffery.holmes.core.util.LoggerFactory;

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
                    return transformer.transform(loader, className, classBeingRedefined, protectionDomain, classfileBuffer);
                } catch (Exception e) {
                    LOGGER.severe("Failed to transform class: " + className);
                }
                LOGGER.info("Succeed to transform class: " + className);
            }
            return classfileBuffer;
        }
    }

}
