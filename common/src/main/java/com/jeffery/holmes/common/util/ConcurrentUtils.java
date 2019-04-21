package com.jeffery.holmes.common.util;

import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

public class ConcurrentUtils {

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
