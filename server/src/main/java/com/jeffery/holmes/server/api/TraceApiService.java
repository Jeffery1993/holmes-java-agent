package com.jeffery.holmes.server.api;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.jeffery.holmes.server.consts.FieldConsts;
import org.apache.lucene.search.*;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

@Service
public class TraceApiService extends AbstractService {

    @Override
    protected Query getDefaultQuery() {
        return SPAN_QUERY;
    }

    /**
     * Search for span data.
     *
     * @param clusterId uuid for cluster
     * @param appId     uuid for application
     * @param traceId   uuid for trace
     * @param page      current page
     * @param pageSize  limit of one page
     * @return span data
     * @throws Exception
     */
    public JSONObject getSpans(String clusterId, String appId, String traceId, Integer page, Integer pageSize) throws Exception {
        Query query = buildQuery(clusterId, appId, traceId);
        page = (page == null || page < 1) ? 1 : page;
        pageSize = (pageSize == null || pageSize < 1) ? 10 : pageSize;
        IndexSearcher indexSearcher = getIndexSearcher();
        TopDocs topDocs = indexSearcher.search(query, page * pageSize);
        JSONObject res = new JSONObject();
        res.put("total", topDocs.totalHits);
        JSONArray hits = toJSONArray(topDocs.scoreDocs, (page - 1) * pageSize, page * pageSize);
        res.put("hits", hits);
        return res;
    }

    private Query buildQuery(String clusterId, String appId, String traceId) {
        BooleanQuery.Builder build = new BooleanQuery.Builder();
        build.add(SPAN_QUERY, BooleanClause.Occur.MUST);
        if (!StringUtils.isEmpty(clusterId)) {
            build.add(buildBooleanClause(FieldConsts.clusterId, clusterId));
        }
        if (!StringUtils.isEmpty(appId)) {
            build.add(buildBooleanClause(FieldConsts.appId, appId));
        }
        if (!StringUtils.isEmpty(traceId)) {
            build.add(buildBooleanClause(FieldConsts.traceId, traceId));
        }
        return build.build();
    }

    /**
     * Search for details for one trace.
     *
     * @param traceId uuid for trace
     * @return details for the trace
     * @throws Exception
     */
    public JSONObject getSpanEvents(String traceId) throws Exception {
        if (StringUtils.isEmpty(traceId)) {
            throw new IllegalArgumentException("traceId cannot be empty!");
        }
        BooleanQuery.Builder build = new BooleanQuery.Builder();
        build.add(SPAN_EVENT_QUERY, BooleanClause.Occur.MUST);
        build.add(buildBooleanClause(FieldConsts.traceId, traceId));
        Query query = build.build();
        IndexSearcher indexSearcher = getIndexSearcher();
        TopDocs topDocs = indexSearcher.search(query, DEFAULT_MAX_HITS_FOR_TRACE);
        JSONObject res = new JSONObject();
        JSONArray hits = toJSONArray(topDocs.scoreDocs);
        // TODO comparator
        res.put("hits", hits);
        return res;
    }

}
