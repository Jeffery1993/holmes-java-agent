package com.jeffery.holmes.core.transformer;

import java.util.Collection;

public abstract class CollectionMatchedTransformer implements HolmesTransformer {

    @Override
    public boolean matches(String className) {
        if (getNames() == null || getNames().isEmpty()) {
            return false;
        }
        return getNames().contains(className);
    }

    public abstract Collection<String> getNames();

}