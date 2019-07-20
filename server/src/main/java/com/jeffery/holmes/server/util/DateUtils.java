package com.jeffery.holmes.server.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Utility for processing date.
 */
public class DateUtils {

    public static final String DEFAULT_LONG_PATTERN = "yyyy-MM-dd HH:mm:ss.SSS";
    public static final String DEFAULT_SHORT_PATTERN = "yyyy-MM-dd HH:mm:ss";

    private static final ThreadLocal<SimpleDateFormat> LONG_PATTERN_THREAD_LOCAL = ThreadLocal.withInitial(
            () -> new SimpleDateFormat(DEFAULT_LONG_PATTERN)
    );

    private static final ThreadLocal<SimpleDateFormat> SHORT_PATTERN_THREAD_LOCAL = ThreadLocal.withInitial(
            () -> new SimpleDateFormat(DEFAULT_SHORT_PATTERN)
    );

    private DateUtils() {
    }

    public static String format(long time) {
        return format(time, false);
    }

    public static String format(long time, boolean mills) {
        return format(new Date(time), mills);
    }

    public static String format(Date date, boolean mills) {
        if (mills) {
            return LONG_PATTERN_THREAD_LOCAL.get().format(date);
        } else {
            return SHORT_PATTERN_THREAD_LOCAL.get().format(date);
        }
    }

    public static Date parse(String str) throws ParseException {
        return SHORT_PATTERN_THREAD_LOCAL.get().parse(str);
    }

}
