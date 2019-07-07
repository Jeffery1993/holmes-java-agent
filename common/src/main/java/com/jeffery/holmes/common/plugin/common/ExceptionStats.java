package com.jeffery.holmes.common.plugin.common;

import com.jeffery.holmes.common.collector.stats.Stats;
import com.jeffery.holmes.common.util.IOUtils;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;

public class ExceptionStats implements Stats {

    private AtomicInteger count = new AtomicInteger(0);
    private AtomicReference<String> stackTrace = new AtomicReference<String>();

    @Override
    public long onStart() {
        throw new UnsupportedOperationException();
    }

    @Override
    public void onThrowable(Throwable throwable) {
        count.getAndIncrement();
        stackTrace.getAndSet(toStackTrace(throwable));
    }

    private String toStackTrace(Throwable throwable) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        PrintStream printStream = new PrintStream(byteArrayOutputStream);
        throwable.printStackTrace(printStream);
        IOUtils.closeQuietly(byteArrayOutputStream);
        IOUtils.closeQuietly(printStream);
        return byteArrayOutputStream.toString();
    }

    @Override
    public long onFinally(long startTime) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Map<String, Object> harvest() {
        Map<String, Object> map = new LinkedHashMap<String, Object>();
        map.put("count", count.getAndSet(0));
        map.put("stackTrace", stackTrace.getAndSet(null));
        return map;
    }

}
