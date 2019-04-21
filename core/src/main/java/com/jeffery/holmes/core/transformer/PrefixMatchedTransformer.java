package com.jeffery.holmes.core.transformer;

public abstract class PrefixMatchedTransformer implements HolmesTransformer {

    @Override
    public boolean matches(String className) {
        if (getPrefixName() == null || getPrefixName().isEmpty()) {
            return false;
        }
        return className.startsWith(getPrefixName());
    }

    public abstract String getPrefixName();

}