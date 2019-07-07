package com.jeffery.holmes.common.plugin.common;

import com.jeffery.holmes.common.collector.aggregator.AbstractAggregator;

import java.security.CodeSource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DefaultVersionAggregator extends AbstractAggregator {

    private String version;

    public void setVersion(String version) {
        this.version = version;
    }

    public void setVersion(CodeSource codeSource) {
        String path = codeSource.getLocation().getPath();
        int index;
        if ((index = path.lastIndexOf("/")) > 0) {
            path = path.substring(index + 1);
        }
        this.version = path;
    }

    @Override
    public String getName() {
        return "version";
    }

    @Override
    public List<Map<String, Object>> harvest() {
        List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("version", version);
        list.add(map);
        return list;
    }

}
