package com.jeffery.holmes.core.transformer;

import java.util.regex.Pattern;

/**
 * An implementation of {@link HolmesTransformer}.
 *
 * <p>Matching when the {@code className} matches the regex pattern, which is provided by subclass.</p>
 */
public abstract class RegexMatchedTransformer implements HolmesTransformer {

    private Pattern pattern;
    private volatile boolean compiled;

    @Override
    public boolean matches(String className) {
        if (getRegexName() == null || getRegexName().isEmpty()) {
            return false;
        }
        if (!compiled) {
            try {
                pattern = Pattern.compile(getRegexName());
            } catch (Exception e) {
                return false;
            } finally {
                compiled = true;
            }
        }
        return pattern.matcher(className).matches();
    }

    /**
     * Get the regex name to match.
     *
     * @return the regex name to match
     */
    public abstract String getRegexName();

}