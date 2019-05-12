package com.jeffery.holmes.core.message;

import com.alibaba.fastjson.JSON;
import com.jeffery.holmes.common.consts.ConfigConsts;
import com.jeffery.holmes.common.util.ConfigManager;

import java.util.HashMap;
import java.util.Map;

public class MessageImpl implements Message {

    private int type;
    private Map<String, Object> header;
    private Object body;

    private static final Map<String, Object> DEFAULT_HEADER = new HashMap<String, Object>();

    static {
        DEFAULT_HEADER.put(ConfigConsts.CLUSTER_ID, ConfigManager.getClusterId());
        DEFAULT_HEADER.put(ConfigConsts.APP_ID, ConfigManager.getAppId());
    }

    public MessageImpl(int type, Object body) {
        this(type, DEFAULT_HEADER, body);
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

    @Override
    public byte[] getHeaderAsBytes() {
        return JSON.toJSONBytes(header);
    }

    @Override
    public byte[] getBodyAsBytes() {
        return JSON.toJSONBytes(body);
    }

}
