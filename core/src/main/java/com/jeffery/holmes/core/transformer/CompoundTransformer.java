package com.jeffery.holmes.core.transformer;

import java.lang.instrument.ClassFileTransformer;
import java.lang.instrument.IllegalClassFormatException;
import java.security.ProtectionDomain;

public class CompoundTransformer implements ClassFileTransformer {

    @Override
    public byte[] transform(ClassLoader loader, String className, Class<?> classBeingRedefined, ProtectionDomain protectionDomain, byte[] classfileBuffer) throws IllegalClassFormatException {
        if (className.startsWith("sun.") || className.startsWith("java.")) {
            return classfileBuffer;
        } else {
            ClassFileTransformer transformer = TransformerManager.getMatchedTransformer(className);
            if (transformer != null) {
                return transformer.transform(loader, className, classBeingRedefined, protectionDomain, classfileBuffer);
            } else {
                return classfileBuffer;
            }
        }
    }

}
