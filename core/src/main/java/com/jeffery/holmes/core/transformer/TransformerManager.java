package com.jeffery.holmes.core.transformer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Manager for transformers.
 */
public class TransformerManager {

    private static final Map<String, HolmesTransformer> ACCURATE_MATCHED_TRANSFORMER_MAP = new HashMap<String, HolmesTransformer>();
    private static final List<PrefixMatchedTransformer> PREFIX_MATCHED_TRANSFORMER_LIST = new ArrayList<PrefixMatchedTransformer>();
    private static final List<RegexMatchedTransformer> REGEX_MATCHED_TRANSFORMER_LIST = new ArrayList<RegexMatchedTransformer>();

    /**
     * Register the holmes transformer.
     *
     * @param transformer the holmes transformer to be registered
     */
    public static void register(HolmesTransformer transformer) {
        if (transformer instanceof AccurateMatchedTransformer) {
            AccurateMatchedTransformer accurateMatchedTransformer = (AccurateMatchedTransformer) transformer;
            ACCURATE_MATCHED_TRANSFORMER_MAP.put(accurateMatchedTransformer.getAccurateName(), accurateMatchedTransformer);
        } else if (transformer instanceof CollectionMatchedTransformer) {
            CollectionMatchedTransformer collectionMatchedTransformer = (CollectionMatchedTransformer) transformer;
            for (String name : collectionMatchedTransformer.getNames()) {
                ACCURATE_MATCHED_TRANSFORMER_MAP.put(name, collectionMatchedTransformer);
            }
        } else if (transformer instanceof PrefixMatchedTransformer) {
            PREFIX_MATCHED_TRANSFORMER_LIST.add((PrefixMatchedTransformer) transformer);
        } else if (transformer instanceof RegexMatchedTransformer) {
            REGEX_MATCHED_TRANSFORMER_LIST.add((RegexMatchedTransformer) transformer);
        } else {
            return;
        }
    }

    /**
     * Get the matched holmes transformer according to the {@code className} provided.
     *
     * @param className class name provided
     * @return the matched holmes transformer or null if no transformer found
     */
    public static HolmesTransformer getMatchedTransformer(String className) {
        HolmesTransformer transformer = null;
        if ((transformer = ACCURATE_MATCHED_TRANSFORMER_MAP.get(className)) != null) {
            return transformer;
        }
        for (PrefixMatchedTransformer prefixMatchedTransformer : PREFIX_MATCHED_TRANSFORMER_LIST) {
            if (prefixMatchedTransformer.matches(className)) {
                return prefixMatchedTransformer;
            }
        }
        for (RegexMatchedTransformer regexMatchedTransformer : REGEX_MATCHED_TRANSFORMER_LIST) {
            if (regexMatchedTransformer.matches(className)) {
                return regexMatchedTransformer;
            }
        }
        return null;
    }

}
