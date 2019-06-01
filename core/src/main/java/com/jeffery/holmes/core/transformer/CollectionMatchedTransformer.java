package com.jeffery.holmes.core.transformer;

import java.util.Collection;

/**
 * An implementation of {@link HolmesTransformer}.
 *
 * <p>Matching when the {@code className} in the names collection, which is provided by subclass.</p>
 */
public abstract class CollectionMatchedTransformer implements HolmesTransformer {

    @Override
    public boolean matches(String className) {
        if (getNames() == null || getNames().isEmpty()) {
            return false;
        }
        return getNames().contains(className);
    }

    /**
     * Get the collection of names to match.
     *
     * @return the collection of names to match
     */
    public abstract Collection<String> getNames();

}