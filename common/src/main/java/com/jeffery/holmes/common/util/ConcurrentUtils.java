package com.jeffery.holmes.common.util;

import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

/**
 * Utility for concurrent operations.
 */
public class ConcurrentUtils {

    /**
     * Set value to target if value &gt target. This method can work properly under concurrent circumstances.
     *
     * @param target target number to be compared and set
     * @param value  value to set
     * @return true if set value to target successfully
     */
    public static boolean setMaxConcurrently(AtomicInteger target, int value) {
        for (; ; ) {
            int expect = target.get();
            if (expect >= value) {
                return false;
            } else {
                if (target.compareAndSet(expect, value)) {
                    return true;
                }
            }
        }
    }

    /**
     * Set value to target if value &lt target. This method can work properly under concurrent circumstances.
     *
     * @param target target number to be compared and set
     * @param value  value to set
     * @return true if set value to target successfully
     */
    public static boolean setMinConcurrently(AtomicInteger target, int value) {
        for (; ; ) {
            int expect = target.get();
            if (expect <= value) {
                return false;
            } else {
                if (target.compareAndSet(expect, value)) {
                    return true;
                }
            }
        }
    }

    /**
     * Set value to target if value &gt target. This method can work properly under concurrent circumstances.
     *
     * @param target target number to be compared and set
     * @param value  value to set
     * @return true if set value to target successfully
     */
    public static boolean setMaxConcurrently(AtomicLong target, long value) {
        for (; ; ) {
            long expect = target.get();
            if (expect >= value) {
                return false;
            } else {
                if (target.compareAndSet(expect, value)) {
                    return true;
                }
            }
        }
    }

    /**
     * Set value to target if value &lt target. This method can work properly under concurrent circumstances.
     *
     * @param target target number to be compared and set
     * @param value  value to set
     * @return true if set value to target successfully
     */
    public static boolean setMinConcurrently(AtomicLong target, long value) {
        for (; ; ) {
            long expect = target.get();
            if (expect <= value) {
                return false;
            } else {
                if (target.compareAndSet(expect, value)) {
                    return true;
                }
            }
        }
    }

}
