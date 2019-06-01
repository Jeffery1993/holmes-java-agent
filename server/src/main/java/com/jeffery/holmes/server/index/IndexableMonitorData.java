package com.jeffery.holmes.server.index;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.jeffery.holmes.server.consts.FieldConsts;
import com.jeffery.holmes.server.message.Message;
import org.apache.lucene.document.*;

/**
 * Indexable monitor data.
 */
public class IndexableMonitorData implements Indexable {

    private Message message;

    public IndexableMonitorData(Message message) {
        this.message = message;
    }

    @Override
    public Document toDocument() {
        Document document = new Document();
        document.add(new NumericDocValuesField(FieldConsts.type, (long) message.getType()));
        JSONObject headers = JSON.parseObject(message.getHeaders());
        document.add(new StringField(FieldConsts.clusterId, headers.getString(FieldConsts.clusterId), Field.Store.YES));
        document.add(new StringField(FieldConsts.appId, headers.getString(FieldConsts.appId), Field.Store.YES));
        document.add(new StringField(FieldConsts.name, headers.getString(FieldConsts.name), Field.Store.YES));
        document.add(new SortedNumericDocValuesField(FieldConsts.timestamp, headers.getLongValue(FieldConsts.timestamp)));
        document.add(new TextField(FieldConsts.body, message.getBody(), Field.Store.YES));
        return document;
    }

}
