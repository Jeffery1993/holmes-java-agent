package com.jeffery.holmes.core.transformer;

import java.lang.instrument.ClassFileTransformer;

public interface HolmesTransformer extends ClassFileTransformer {

    boolean matches(String className);

}
