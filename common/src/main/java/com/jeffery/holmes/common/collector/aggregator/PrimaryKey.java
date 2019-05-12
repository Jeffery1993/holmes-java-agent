package com.jeffery.holmes.common.collector.aggregator;

import java.util.Arrays;

public class PrimaryKey {

    private String[] keys;

    public PrimaryKey(String... keys) {
        this.keys = keys;
    }

    public int size() {
        return keys == null ? 0 : keys.length;
    }

    public String get(int i) {
        return keys[i];
    }

    @Override
    public String toString() {
        return Arrays.toString(keys);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        PrimaryKey that = (PrimaryKey) o;
        return Arrays.equals(keys, that.keys);
    }

    @Override
    public int hashCode() {
        return Arrays.hashCode(keys);
    }

}
