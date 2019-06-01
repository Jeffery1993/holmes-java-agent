package com.jeffery.holmes.server.api;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.jeffery.holmes.server.consts.FieldConsts;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.Term;
import org.apache.lucene.search.*;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
public class OpenApiService {

    private static final int DEFAULT_MAX_HITS = 100;

    public JSONObject searchMonitorData(String clusterId, String appId, String name, String startTime, String endTime) throws Exception {
        TermQuery clusterIdTerm = new TermQuery(new Term(FieldConsts.clusterId, clusterId));
        TermQuery appIdTerm = new TermQuery(new Term(FieldConsts.appId, appId));
        TermQuery nameTerm = new TermQuery(new Term(FieldConsts.name, name));
        BooleanQuery booleanQuery = new BooleanQuery.Builder()
                .add(clusterIdTerm, BooleanClause.Occur.MUST)
                .add(appIdTerm, BooleanClause.Occur.MUST)
                .add(nameTerm, BooleanClause.Occur.MUST).
                        build();

        TopDocs topDocs = IndexSearcherFactory.getIndexSearcher().search(booleanQuery, DEFAULT_MAX_HITS);
        return toJSONObject(topDocs);
    }


    public JSONObject searchTraceData(String clusterId, String appId, String traceId) throws Exception {
        TermQuery clusterIdTerm = new TermQuery(new Term(FieldConsts.clusterId, clusterId));
        TermQuery appIdTerm = new TermQuery(new Term(FieldConsts.appId, appId));
        TermQuery traceIdTerm = new TermQuery(new Term(FieldConsts.traceId, traceId));
        BooleanQuery booleanQuery = new BooleanQuery.Builder()
                .add(clusterIdTerm, BooleanClause.Occur.MUST)
                .add(appIdTerm, BooleanClause.Occur.MUST)
                .add(traceIdTerm, BooleanClause.Occur.MUST).
                        build();

        TopDocs topDocs = IndexSearcherFactory.getIndexSearcher().search(booleanQuery, DEFAULT_MAX_HITS);
        return toJSONObject(topDocs);
    }

    private JSONObject toJSONObject(TopDocs topDocs) throws IOException {
        JSONObject res = new JSONObject();
        res.put("total", topDocs.totalHits);
        JSONArray hits = new JSONArray();
        for (ScoreDoc scoreDoc : topDocs.scoreDocs) {
            Document document = IndexSearcherFactory.getIndexSearcher().doc(scoreDoc.doc);
            hits.add(document.get(FieldConsts.body));
        }
        res.put("hits", hits);
        return res;
    }

}
