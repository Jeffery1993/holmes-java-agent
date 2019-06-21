package com.jeffery.holmes.server.api;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.jeffery.holmes.server.consts.CollectorEnum;
import com.jeffery.holmes.server.consts.DataCategoryEnum;
import com.jeffery.holmes.server.consts.FieldConsts;
import org.apache.lucene.document.LongPoint;
import org.apache.lucene.search.*;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
public class MonitorApiService extends AbstractService {

    @Override
    protected Query getDefaultQuery() {
        return MONITOR_QUERY;
    }

    /**
     * Get a list of collectors.
     *
     * @return collectors
     */
    public JSONObject getCollectors() {
        JSONObject res = new JSONObject();
        List<Object> collectors = Stream.of(CollectorEnum.values()).map(m -> m.toString()).collect(Collectors.toList());
        JSONArray hits = new JSONArray(collectors);
        res.put("hits", hits);
        return res;
    }

    /**
     * Search for monitor data.
     *
     * @param appId     uuid for application
     * @param collector collector name
     * @param startTime start time
     * @param endTime   end time
     * @return monitor data
     * @throws Exception
     */
    public JSONObject getMonitorData(String appId, CollectorEnum collector, Long startTime, Long endTime) throws Exception {
        Query query = buildQuery(appId, collector, startTime, endTime);
        Sort sort = new Sort(new SortField(FieldConsts.timestamp, SortField.Type.LONG));
        TopDocs topDocs = getIndexSearcher().search(query, DEFAULT_MAX_HITS_FOR_MONITOR, sort);
        return toJSONObject(topDocs);
    }

    private Query buildQuery(String appId, CollectorEnum collector, Long startTime, Long endTime) {
        BooleanQuery.Builder build = new BooleanQuery.Builder();
        Query monitorQuery = LongPoint.newExactQuery(FieldConsts.type, (long) DataCategoryEnum.MONITOR.getCode());
        build.add(monitorQuery, BooleanClause.Occur.MUST);
        build.add(buildBooleanClause(FieldConsts.appId, appId));
        build.add(buildBooleanClause(FieldConsts.name, collector.toString()));
        long now = System.currentTimeMillis();
        startTime = (startTime == null) ? now - 20 * 60 * 1000 : startTime;
        endTime = (endTime == null) ? now : endTime;
        build.add(LongPoint.newRangeQuery(FieldConsts.timestamp, startTime, endTime), BooleanClause.Occur.MUST);
        return build.build();
    }

}
