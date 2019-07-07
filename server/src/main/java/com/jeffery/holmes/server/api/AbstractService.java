package com.jeffery.holmes.server.api;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.jeffery.holmes.server.consts.DataCategoryEnum;
import com.jeffery.holmes.server.consts.FieldConsts;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.LongPoint;
import org.apache.lucene.index.Term;
import org.apache.lucene.search.*;
import org.apache.lucene.search.grouping.GroupingSearch;
import org.apache.lucene.search.grouping.TopGroups;
import org.apache.lucene.util.BytesRef;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public abstract class AbstractService {

    protected Logger logger = LoggerFactory.getLogger(this.getClass());

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
        res.put("hits", getGroupValues(topGroups));
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
        res.put("hits", getGroupValues(topGroups));
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

    private List<Object> getGroupValues(TopGroups<BytesRef> topGroups) {
        return Stream.of(topGroups.groups)
                .map(groupDocs -> groupDocs.groupValue.utf8ToString())
                .collect(Collectors.toList());
    }

    protected BooleanClause buildBooleanClause(String field, String value) {
        TermQuery termQuery = new TermQuery(new Term(field, value));
        return new BooleanClause(termQuery, BooleanClause.Occur.MUST);
    }

    protected <T> List<T> toObjectList(ScoreDoc[] scoreDoc, Class<T> clazz) throws IOException {
        return toObjectList(scoreDoc, 0, scoreDoc.length, clazz);
    }

    protected <T> List<T> toObjectList(ScoreDoc[] scoreDocs, int start, int end, Class<T> clazz) throws IOException {
        List<T> list = new ArrayList<>();
        IndexSearcher indexSearcher = getIndexSearcher();
        start = (start < 0) ? 0 : start;
        end = (end > scoreDocs.length) ? scoreDocs.length : end;
        for (int i = start; i < end; i++) {
            Document document = indexSearcher.doc(scoreDocs[i].doc);
            T body = JSON.parseObject(document.get(FieldConsts.body), clazz);
            list.add(body);
        }
        return list;
    }

    protected List<JSONObject> toObjectList(ScoreDoc[] scoreDoc) throws IOException {
        return toObjectList(scoreDoc, 0, scoreDoc.length);
    }

    protected List<JSONObject> toObjectList(ScoreDoc[] scoreDocs, int start, int end) throws IOException {
        List<JSONObject> list = new ArrayList<>();
        IndexSearcher indexSearcher = getIndexSearcher();
        start = (start < 0) ? 0 : start;
        end = (end > scoreDocs.length) ? scoreDocs.length : end;
        for (int i = start; i < end; i++) {
            Document document = indexSearcher.doc(scoreDocs[i].doc);
            JSONObject body = JSON.parseObject(document.get(FieldConsts.body));
            list.add(body);
        }
        return list;
    }

}
