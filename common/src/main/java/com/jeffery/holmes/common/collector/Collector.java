package com.jeffery.holmes.common.collector;

import java.util.Map;

public interface Collector<T> {

    String getName();

    boolean isEnabled();

    Collector add(T item);

    Map<String, Object> collect();

}
