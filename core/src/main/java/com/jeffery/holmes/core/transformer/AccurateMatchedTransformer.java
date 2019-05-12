package com.jeffery.holmes.core.transformer;

import com.jeffery.holmes.common.util.LoggerFactory;

import java.util.logging.Logger;

public abstract class AccurateMatchedTransformer implements HolmesTransformer {

    protected Logger logger = LoggerFactory.getLogger(this.getClass());

    @Override
    public boolean matches(String className) {
        return className.equals(getAccurateName());
    }

    public abstract String getAccurateName();

}
