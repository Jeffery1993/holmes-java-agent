package com.jeffery.holmes.core.transformer;

public abstract class AccurateMatchedTransformer implements HolmesTransformer {

    @Override
    public boolean matches(String className) {
        return className.equals(getAccurateName());
    }

    public abstract String getAccurateName();

}
