package com.jeffery.holmes.server.api;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.jeffery.holmes.server.consts.CollectorEnum;
import com.jeffery.holmes.server.consts.FieldConsts;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.Term;
import org.apache.lucene.search.*;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.io.IOException;

@Service
public class OpenApiService {

    private static final int DEFAULT_MAX_HITS = 100;

    /**
     * Search for monitor data.
     *
     * @param clusterId uuid for cluster
     * @param appId     uuid for application
     * @param name      collector name
     * @param startTime start time
     * @param endTime   end time
     * @param pretty    json parsed
     * @return monitor data
     * @throws Exception
     */
    public JSONObject searchMonitorData(String clusterId, String appId, CollectorEnum name, String startTime, String endTime, boolean pretty) throws Exception {
        Query query = buildQuery(clusterId, appId, name, startTime, endTime);
        TopDocs topDocs = IndexSearcherFactory.getIndexSearcher().search(query, DEFAULT_MAX_HITS);
        return toJSONObject(topDocs, pretty);
    }

    private Query buildQuery(String clusterId, String appId, CollectorEnum name, String startTime, String endTime) {
        BooleanQuery.Builder build = new BooleanQuery.Builder();
        if (!StringUtils.isEmpty(clusterId)) {
            TermQuery clusterIdTerm = new TermQuery(new Term(FieldConsts.clusterId, clusterId));
            build.add(clusterIdTerm, BooleanClause.Occur.MUST);
        }
        if (!StringUtils.isEmpty(appId)) {
            TermQuery appIdTerm = new TermQuery(new Term(FieldConsts.appId, appId));
            build.add(appIdTerm, BooleanClause.Occur.MUST);
        }
        if (name != null) {
            TermQuery nameTerm = new TermQuery(new Term(FieldConsts.name, name.toString()));
            build.add(nameTerm, BooleanClause.Occur.MUST);
        }
        return build.build();
    }

    /**
     * Search for trace data.
     *
     * @param clusterId uuid for cluster
     * @param appId     uuid for application
     * @param traceId   uuid for trace
     * @param pretty    json parsed
     * @return trace data
     * @throws Exception
     */
    public JSONObject searchTraceData(String clusterId, String appId, String traceId, boolean pretty) throws Exception {
        Query query = buildQuery(clusterId, appId, traceId);
        TopDocs topDocs = IndexSearcherFactory.getIndexSearcher().search(query, DEFAULT_MAX_HITS);
        return toJSONObject(topDocs, pretty);
    }

    private Query buildQuery(String clusterId, String appId, String traceId) {
        BooleanQuery.Builder build = new BooleanQuery.Builder();
        if (!StringUtils.isEmpty(clusterId)) {
            TermQuery clusterIdTerm = new TermQuery(new Term(FieldConsts.clusterId, clusterId));
            build.add(clusterIdTerm, BooleanClause.Occur.MUST);
        }
        if (!StringUtils.isEmpty(appId)) {
            TermQuery appIdTerm = new TermQuery(new Term(FieldConsts.appId, appId));
            build.add(appIdTerm, BooleanClause.Occur.MUST);
        }
        if (!StringUtils.isEmpty(traceId)) {
            TermQuery traceIdTerm = new TermQuery(new Term(FieldConsts.traceId, traceId));
            build.add(traceIdTerm, BooleanClause.Occur.MUST);
        }
        return build.build();
    }

    private JSONObject toJSONObject(TopDocs topDocs, boolean pretty) throws IOException {
        JSONObject res = new JSONObject();
        res.put("total", topDocs.totalHits);
        JSONArray hits = new JSONArray();
        for (ScoreDoc scoreDoc : topDocs.scoreDocs) {
            Document document = IndexSearcherFactory.getIndexSearcher().doc(scoreDoc.doc);
            String body = document.get(FieldConsts.body);
            if (pretty) {
                hits.add(JSON.parseObject(body));
            } else {
                hits.add(body);
            }
        }
        res.put("hits", hits);
        return res;
    }

}
