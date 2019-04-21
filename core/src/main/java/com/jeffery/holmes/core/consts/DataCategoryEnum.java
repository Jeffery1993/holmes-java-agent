package com.jeffery.holmes.core.consts;

public enum DataCategoryEnum {

    MONITOR(0), SPAN(1), SPAN_EVENT(2);

    private int code;

    private DataCategoryEnum(int code) {
        this.code = code;
    }

    public int getCode() {
        return code;
    }

}
