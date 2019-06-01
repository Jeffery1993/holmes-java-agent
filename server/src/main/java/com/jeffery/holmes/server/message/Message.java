package com.jeffery.holmes.server.message;

import com.jeffery.holmes.server.index.Indexable;
import com.jeffery.holmes.server.index.IndexableMonitorData;
import com.jeffery.holmes.server.index.IndexableTraceData;
import org.apache.lucene.document.Document;

/**
 * Message that can be transformed to {@link Document};
 */
public class Message implements Indexable {

    private int type;
    private String headers;
    private String body;

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getHeaders() {
        return headers;
    }

    public void setHeaders(String headers) {
        this.headers = headers;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    @Override
    public Document toDocument() {
        switch (type) {
            case 0:
                return new IndexableMonitorData(this).toDocument();
            case 1:
            case 2:
                return new IndexableTraceData(this).toDocument();
            default:
                return null;
        }
    }

}
