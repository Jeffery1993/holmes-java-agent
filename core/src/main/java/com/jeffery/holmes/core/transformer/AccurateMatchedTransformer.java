package com.jeffery.holmes.core.transformer;

import com.jeffery.holmes.common.util.LoggerFactory;

import java.util.logging.Logger;

/**
 * An implementation of {@link HolmesTransformer}.
 *
 * <p>Matching when the {@code className} equals to the accurate name, which is provided by subclass.</p>
 */
public abstract class AccurateMatchedTransformer implements HolmesTransformer {

    protected Logger logger = LoggerFactory.getLogger(this.getClass());

    @Override
    public boolean matches(String className) {
        return className.equals(getAccurateName());
    }

    /**
     * Get the accurate name to match.
     *
     * @return the accurate name to match
     */
    public abstract String getAccurateName();

}
