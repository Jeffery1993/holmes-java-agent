package com.jeffery.holmes.core.message;

import java.util.HashMap;
import java.util.Map;

public class MessageImpl implements Message {

    private int type;
    private Map<String, Object> header;
    private Object body;

    public MessageImpl() {
    }

    public MessageImpl(int type, Object body) {
        this(type, new HashMap<String, Object>(), body);
    }

    public MessageImpl(int type, Map<String, Object> header, Object body) {
        this.type = type;
        this.header = header;
        this.body = body;
    }

    @Override
    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    @Override
    public Map<String, Object> getHeader() {
        return header;
    }

    public void setHeader(Map<String, Object> header) {
        this.header = header;
    }

    @Override
    public Object getBody() {
        return body;
    }

    public void setBody(Object body) {
        this.body = body;
    }

}
