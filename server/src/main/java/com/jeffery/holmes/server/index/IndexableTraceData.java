package com.jeffery.holmes.server.index;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.jeffery.holmes.server.consts.FieldConsts;
import com.jeffery.holmes.server.message.Message;
import org.apache.lucene.document.*;
import org.apache.lucene.util.BytesRef;

/**
 * Indexable trace data.
 */
public class IndexableTraceData implements Indexable {

    private Message message;

    public IndexableTraceData(Message message) {
        this.message = message;
    }

    @Override
    public Document toDocument() {
        Document document = new Document();
        document.add(new LongPoint(FieldConsts.type, (long) message.getType()));
        JSONObject headers = JSON.parseObject(message.getHeaders());
        document.add(new StringField(FieldConsts.clusterId, headers.getString(FieldConsts.clusterId), Field.Store.YES));
        document.add(new SortedDocValuesField(FieldConsts.clusterId, new BytesRef(headers.getString(FieldConsts.clusterId))));
        document.add(new StringField(FieldConsts.appId, headers.getString(FieldConsts.appId), Field.Store.YES));
        document.add(new SortedDocValuesField(FieldConsts.appId, new BytesRef(headers.getString(FieldConsts.appId))));
        document.add(new StringField(FieldConsts.traceId, headers.getString(FieldConsts.traceId), Field.Store.YES));
        document.add(new TextField(FieldConsts.body, message.getBody(), Field.Store.YES));
        return document;
    }

}
