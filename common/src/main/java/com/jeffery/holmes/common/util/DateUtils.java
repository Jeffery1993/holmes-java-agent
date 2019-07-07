package com.jeffery.holmes.common.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DateUtils {

    public static final String DEFAULT_PATTERN = "yyyy-MM-dd HH:mm:ss";

    private static final ThreadLocal<SimpleDateFormat> DEFAULT_THREAD_LOCAL = new ThreadLocal<SimpleDateFormat>() {
        @Override
        public SimpleDateFormat initialValue() {
            return new SimpleDateFormat(DEFAULT_PATTERN);
        }
    };

    private DateUtils() {
    }

    public static String format(long time) {
        return format(new Date(time));
    }

    public static String format(Date date) {
        return DEFAULT_THREAD_LOCAL.get().format(date);
    }

    public static Date parse(String str) throws ParseException {
        return DEFAULT_THREAD_LOCAL.get().parse(str);
    }

}
