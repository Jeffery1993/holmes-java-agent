package com.jeffery.holmes.server.consts;

/**
 * Enum for data categories.
 */
public enum DataCategoryEnum {

    MONITOR(0), SPAN(1), SPAN_EVENT(2);

    private int code;

    DataCategoryEnum(int code) {
        this.code = code;
    }

    public int getCode() {
        return code;
    }

}
