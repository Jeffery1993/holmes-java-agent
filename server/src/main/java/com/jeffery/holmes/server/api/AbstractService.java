package com.jeffery.holmes.server.api;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.jeffery.holmes.server.consts.DataCategoryEnum;
import com.jeffery.holmes.server.consts.FieldConsts;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.LongPoint;
import org.apache.lucene.index.Term;
import org.apache.lucene.search.*;
import org.apache.lucene.search.grouping.GroupDocs;
import org.apache.lucene.search.grouping.GroupingSearch;
import org.apache.lucene.search.grouping.TopGroups;
import org.apache.lucene.util.BytesRef;
import org.springframework.util.StringUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public abstract class AbstractService {

    protected static final int DEFAULT_MAX_HITS_FOR_MONITOR = 1000;
    protected static final int DEFAULT_MAX_HITS_FOR_TRACE = 100;

    protected static final Query MONITOR_QUERY = LongPoint.newExactQuery(FieldConsts.type, (long) DataCategoryEnum.MONITOR.getCode());
    protected static final Query SPAN_QUERY = LongPoint.newExactQuery(FieldConsts.type, (long) DataCategoryEnum.SPAN.getCode());
    protected static final Query SPAN_EVENT_QUERY = LongPoint.newExactQuery(FieldConsts.type, (long) DataCategoryEnum.SPAN_EVENT.getCode());

    protected abstract Query getDefaultQuery();

    protected IndexSearcher getIndexSearcher() {
        return IndexSearcherFactory.getIndexSearcher();
    }

    /**
     * Search for clusters.
     *
     * @return clusters
     * @throws Exception
     */
    public JSONObject getClusters() throws Exception {
        Query query = getDefaultQuery();
        TopGroups<BytesRef> topGroups = groupSearch(FieldConsts.clusterId, query, getIndexSearcher());
        JSONObject res = new JSONObject();
        JSONArray hits = new JSONArray(getAllGroupValues(topGroups));
        res.put("hits", hits);
        return res;
    }

    /**
     * Search for applications.
     *
     * @param clusterId uuid for cluster
     * @return applications
     * @throws Exception
     */
    public JSONObject getApps(String clusterId) throws Exception {
        BooleanQuery.Builder build = new BooleanQuery.Builder();
        build.add(getDefaultQuery(), BooleanClause.Occur.MUST);
        if (!StringUtils.isEmpty(clusterId)) {
            build.add(buildBooleanClause(FieldConsts.clusterId, clusterId));
        }
        Query query = build.build();
        TopGroups<BytesRef> topGroups = groupSearch(FieldConsts.appId, query, getIndexSearcher());
        JSONObject res = new JSONObject();
        JSONArray hits = new JSONArray(getAllGroupValues(topGroups));
        res.put("hits", hits);
        return res;
    }

    private TopGroups<BytesRef> groupSearch(String groupField, Query query, IndexSearcher indexSearcher) throws IOException {
        GroupingSearch groupingSearch = new GroupingSearch(groupField);
        groupingSearch.setGroupSort(new Sort(SortField.FIELD_DOC));
        groupingSearch.setFillSortFields(true);
        groupingSearch.setCachingInMB(4.0, true);
        groupingSearch.setAllGroups(true);
        groupingSearch.setGroupDocsLimit(10);
        return groupingSearch.search(indexSearcher, query, 0, 1000);
    }

    private List<Object> getAllGroupValues(TopGroups<BytesRef> topGroups) {
        List<Object> list = new ArrayList<>();
        for (GroupDocs<BytesRef> groupDocs : topGroups.groups) {
            list.add(groupDocs.groupValue.utf8ToString());
        }
        return list;
    }

    protected BooleanClause buildBooleanClause(String field, String value) {
        TermQuery termQuery = new TermQuery(new Term(field, value));
        return new BooleanClause(termQuery, BooleanClause.Occur.MUST);
    }

    protected JSONArray toJSONArray(ScoreDoc[] scoreDoc) throws IOException {
        return toJSONArray(scoreDoc, 0, scoreDoc.length);
    }

    protected JSONArray toJSONArray(ScoreDoc[] scoreDocs, int start, int end) throws IOException {
        JSONArray hits = new JSONArray();
        IndexSearcher indexSearcher = getIndexSearcher();
        start = (start < 0) ? 0 : start;
        end = (end > scoreDocs.length) ? scoreDocs.length : end;
        for (int i = start; i < end; i++) {
            Document document = indexSearcher.doc(scoreDocs[i].doc);
            JSONObject body = JSON.parseObject(document.get(FieldConsts.body));
            hits.add(body);
        }
        return hits;
    }

    protected JSONObject toJSONObject(TopDocs topDocs) throws IOException {
        JSONObject res = new JSONObject();
        res.put("total", topDocs.totalHits);
        JSONArray hits = toJSONArray(topDocs.scoreDocs);
        res.put("hits", hits);
        return res;
    }

}
