package com.jeffery.holmes.server.api;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.jeffery.holmes.server.consts.CollectorEnum;
import com.jeffery.holmes.server.consts.DataCategoryEnum;
import com.jeffery.holmes.server.consts.FieldConsts;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.LongPoint;
import org.apache.lucene.index.Term;
import org.apache.lucene.search.*;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.io.IOException;

@Service
public class OpenApiService {

    private static final int DEFAULT_MAX_HITS_FOR_MONITOR = 1000;
    private static final int DEFAULT_MAX_HITS_FOR_TRACE = 100;

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
    public JSONObject searchMonitorData(String clusterId, String appId, CollectorEnum name, Long startTime, Long endTime, boolean pretty) throws Exception {
        Query query = buildQuery(clusterId, appId, name, startTime, endTime);
        Sort sort = new Sort(new SortField(FieldConsts.timestamp, SortField.Type.LONG));
        TopDocs topDocs = IndexSearcherFactory.getIndexSearcher().search(query, DEFAULT_MAX_HITS_FOR_MONITOR, sort);
        return toJSONObject(topDocs, pretty);
    }

    private Query buildQuery(String clusterId, String appId, CollectorEnum name, Long startTime, Long endTime) {
        BooleanQuery.Builder build = new BooleanQuery.Builder();
        Query monitorQuery = LongPoint.newExactQuery(FieldConsts.type, (long) DataCategoryEnum.MONITOR.getCode());
        build.add(monitorQuery, BooleanClause.Occur.MUST);
        if (!StringUtils.isEmpty(clusterId)) {
            build.add(buildClause(FieldConsts.clusterId, clusterId));
        }
        if (!StringUtils.isEmpty(appId)) {
            build.add(buildClause(FieldConsts.appId, appId));
        }
        if (name != null) {
            build.add(buildClause(FieldConsts.name, name.toString()));
        }
        long now = System.currentTimeMillis();
        startTime = (startTime == null) ? now - 20 * 60 * 1000 : startTime;
        endTime = (endTime == null) ? now : endTime;
        build.add(LongPoint.newRangeQuery(FieldConsts.timestamp, startTime, endTime), BooleanClause.Occur.MUST);
        return build.build();
    }

    private BooleanClause buildClause(String field, String value) {
        TermQuery termQuery = new TermQuery(new Term(field, value));
        return new BooleanClause(termQuery, BooleanClause.Occur.MUST);
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
        TopDocs topDocs = IndexSearcherFactory.getIndexSearcher().search(query, DEFAULT_MAX_HITS_FOR_TRACE);
        return toJSONObject(topDocs, pretty);
    }

    private Query buildQuery(String clusterId, String appId, String traceId) {
        BooleanQuery.Builder build = new BooleanQuery.Builder();
        Query traceQuery = LongPoint.newExactQuery(FieldConsts.type, (long) DataCategoryEnum.SPAN_EVENT.getCode());
        build.add(traceQuery, BooleanClause.Occur.MUST);
        if (!StringUtils.isEmpty(clusterId)) {
            build.add(buildClause(FieldConsts.clusterId, clusterId));
        }
        if (!StringUtils.isEmpty(appId)) {
            build.add(buildClause(FieldConsts.appId, appId));
        }
        if (!StringUtils.isEmpty(traceId)) {
            build.add(buildClause(FieldConsts.traceId, traceId));
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
