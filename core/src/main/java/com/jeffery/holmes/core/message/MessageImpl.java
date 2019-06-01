package com.jeffery.holmes.core.message;

import com.alibaba.fastjson.JSON;
import com.jeffery.holmes.common.consts.ConfigConsts;
import com.jeffery.holmes.common.util.ConfigManager;

import java.util.HashMap;
import java.util.Map;

/**
 * Implementation of {@link Message}.
 */
public class MessageImpl implements Message {

    private int type;
    private Map<String, Object> headers;
    private Object body;

    private static final HashMap<String, Object> DEFAULT_HEADER = new HashMap<String, Object>();

    static {
        DEFAULT_HEADER.put(ConfigConsts.CLUSTER_ID, ConfigManager.getClusterId());
        DEFAULT_HEADER.put(ConfigConsts.APP_ID, ConfigManager.getAppId());
    }

    public MessageImpl(int type, Object body) {
        this(type, (Map<String, Object>) DEFAULT_HEADER.clone(), body);
    }

    public MessageImpl(int type, Map<String, Object> headers, Object body) {
        this.type = type;
        this.headers = headers;
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
    public Map<String, Object> getHeaders() {
        return headers;
    }

    public void setHeaders(Map<String, Object> headers) {
        this.headers = headers;
    }

    @Override
    public Object getHeader(String key) {
        return headers.get(key);
    }

    @Override
    public void addHeader(String key, Object value) {
        headers.put(key, value);
    }

    @Override
    public Object getBody() {
        return body;
    }

    public void setBody(Object body) {
        this.body = body;
    }

    @Override
    public byte[] getHeadersAsBytes() {
        return JSON.toJSONBytes(headers);
    }

    @Override
    public byte[] getBodyAsBytes() {
        return JSON.toJSONBytes(body);
    }

}
