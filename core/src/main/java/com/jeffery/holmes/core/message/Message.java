package com.jeffery.holmes.core.message;

import java.util.Map;

/**
 * Interface for message.
 */
public interface Message {

    /**
     * Get the type of message.
     *
     * @return message type
     */
    int getType();

    /**
     * Get the headers of message.
     *
     * @return message headers
     */
    Map<String, Object> getHeaders();

    /**
     * Get the value of header by key.
     *
     * @param key key of the header
     * @return value of the header
     */
    Object getHeader(String key);

    /**
     * Add the key-value header to the headers.
     *
     * @param key   key of the header
     * @param value value of the header
     */
    void addHeader(String key, Object value);

    /**
     * Get the body of message.
     *
     * @return message body
     */
    Object getBody();

    /**
     * Get the header of message as byte array.
     *
     * @return message header as byte array
     */
    byte[] getHeadersAsBytes();

    /**
     * Get the body of message as byte array.
     *
     * @return message body as byte array
     */
    byte[] getBodyAsBytes();

}
