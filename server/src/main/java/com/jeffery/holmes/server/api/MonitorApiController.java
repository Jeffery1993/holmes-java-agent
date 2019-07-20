package com.jeffery.holmes.server.api;

import com.alibaba.fastjson.JSONObject;
import com.jeffery.holmes.server.consts.CollectorEnum;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * Open API for searching monitor data.
 */
@RestController
@RequestMapping(value = "/api/monitor", produces = "application/json; charset=utf-8")
public class MonitorApiController {

    private static final Logger LOGGER = LoggerFactory.getLogger(TraceApiController.class);

    @Autowired
    private MonitorApiService monitorApiService;

    @RequestMapping(value = "/clusters", method = RequestMethod.GET)
    public JSONObject getClusters() {
        try {
            return monitorApiService.getClusters();
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
        }
        return new JSONObject();
    }

    @RequestMapping(value = "/apps", method = RequestMethod.GET)
    public JSONObject getApps(@RequestParam(required = false) String clusterId) {
        try {
            return monitorApiService.getApps(clusterId);
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
        }
        return new JSONObject();
    }

    @RequestMapping(value = "/collectors", method = RequestMethod.GET)
    public JSONObject getCollectors() {
        try {
            return monitorApiService.getCollectors();
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
        }
        return new JSONObject();
    }

    @RequestMapping(value = "/monitorData", method = RequestMethod.GET)
    public JSONObject getMonitorData(@RequestParam(required = true) String appId,
                                     @RequestParam(required = true) CollectorEnum collector,
                                     @RequestParam(required = false) Long startTime,
                                     @RequestParam(required = false) Long endTime,
                                     @RequestParam(required = false) boolean raw) {
        try {
            return monitorApiService.getMonitorData(appId, collector, startTime, endTime, raw);
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
        }
        return new JSONObject();
    }

}
