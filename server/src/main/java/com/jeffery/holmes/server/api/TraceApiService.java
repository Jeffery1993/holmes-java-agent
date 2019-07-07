package com.jeffery.holmes.server.api;

import com.alibaba.fastjson.JSONObject;
import com.jeffery.holmes.server.consts.FieldConsts;
import org.apache.lucene.search.*;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.stream.Collectors;

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
        List<JSONObject> hits = toObjectList(topDocs.scoreDocs, (page - 1) * pageSize, page * pageSize);
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
        List<JSONObject> hits = toObjectList(topDocs.scoreDocs)
                .stream()
                .map(m -> new SpanEventWrapper(m))
                .sorted()
                .map(SpanEventWrapper::getBody)
                .collect(Collectors.toList());
        res.put("hits", hits);
        return res;
    }

    static final class SpanEventWrapper implements Comparable<SpanEventWrapper> {

        private final JSONObject body;
        private final int[] spanIdArray;
        private final int[] spanEventIdArray;

        SpanEventWrapper(JSONObject body) {
            this.body = body;
            this.spanIdArray = parseIntArray(body.getString("spanId"), "-");
            this.spanEventIdArray = parseIntArray(body.getString("spanEventId"), "-");
        }

        private int[] parseIntArray(String str, String regex) {
            if (StringUtils.isEmpty(str)) {
                return new int[0];
            }
            String[] arr = str.split(regex);
            int[] intArray = new int[arr.length];
            for (int i = 0; i < arr.length; i++) {
                try {
                    intArray[i] = Integer.parseInt(arr[i]);
                } catch (NumberFormatException e) {
                    intArray[i] = 1;
                }
            }
            return intArray;
        }

        public JSONObject getBody() {
            return body;
        }

        @Override
        public int compareTo(SpanEventWrapper o) {
            int diff = compare(this.spanIdArray, o.spanIdArray);
            if (diff != 0) {
                return diff;
            }
            return compare(this.spanEventIdArray, o.spanEventIdArray);
        }

        private int compare(int[] arr1, int[] arr2) {
            for (int i = 0; i < arr1.length && i < arr2.length; i++) {
                if (arr1[i] != arr2[i]) {
                    return arr1[i] - arr2[i];
                }
            }
            if (arr1.length != arr2.length) {
                return arr1.length - arr2.length;
            }
            return 0;
        }

    }

}
