package com.jeffery.holmes.core.message;

import java.util.Map;

public interface Message {

    int getType();

    Map<String, Object> getHeader();

    Object getBody();

    byte[] getHeaderAsBytes();

    byte[] getBodyAsBytes();

}
