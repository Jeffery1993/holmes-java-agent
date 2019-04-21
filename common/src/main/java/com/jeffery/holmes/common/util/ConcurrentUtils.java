package com.jeffery.holmes.common.util;

import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

public class ConcurrentUtils {

    public static void setMaxConcurrently(AtomicInteger target, int value) {
        for (; ; ) {
            int expect = target.get();
            if (expect >= value) {
                break;
            } else {
                if (target.compareAndSet(expect, value)) {
                    break;
                }
            }
        }
    }

    public static void setMaxConcurrently(AtomicLong target, long value) {
        for (; ; ) {
            long expect = target.get();
            if (expect >= value) {
                break;
            } else {
                if (target.compareAndSet(expect, value)) {
                    break;
                }
            }
        }
    }

    public static void setMinConcurrently(AtomicInteger target, int value) {
        for (; ; ) {
            int expect = target.get();
            if (expect <= value) {
                break;
            } else {
                if (target.compareAndSet(expect, value)) {
                    break;
                }
            }
        }
    }

    public static void setMinConcurrently(AtomicLong target, long value) {
        for (; ; ) {
            long expect = target.get();
            if (expect <= value) {
                break;
            } else {
                if (target.compareAndSet(expect, value)) {
                    break;
                }
            }
        }
    }

}
