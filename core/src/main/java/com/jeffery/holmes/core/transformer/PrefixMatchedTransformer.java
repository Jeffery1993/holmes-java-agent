package com.jeffery.holmes.core.transformer;

/**
 * An implementation of {@link HolmesTransformer}.
 *
 * <p>Matching when the {@code className} starts with the prefix name, which is provided by subclass.</p>
 */
public abstract class PrefixMatchedTransformer implements HolmesTransformer {

    @Override
    public boolean matches(String className) {
        if (getPrefixName() == null || getPrefixName().isEmpty()) {
            return false;
        }
        return className.startsWith(getPrefixName());
    }

    /**
     * Get the prefix name to match.
     *
     * @return the prefix name to match
     */
    public abstract String getPrefixName();

}