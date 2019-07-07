package com.jeffery.holmes.server.api;

import com.alibaba.fastjson.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * Open API for searching trace data.
 */
@RestController
@RequestMapping(value = "/api/trace", produces = "application/json; charset=utf-8")
public class TraceApiController {

    private static final Logger LOGGER = LoggerFactory.getLogger(TraceApiController.class);

    @Autowired
    private TraceApiService traceApiService;

    @RequestMapping(value = "/clusters", method = RequestMethod.GET)
    public JSONObject getClusters() {
        try {
            return traceApiService.getClusters();
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
        }
        return new JSONObject();
    }

    @RequestMapping(value = "/apps", method = RequestMethod.GET)
    public JSONObject getApps(@RequestParam(required = false) String clusterId) {
        try {
            return traceApiService.getApps(clusterId);
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
        }
        return new JSONObject();
    }

    @RequestMapping(value = "/spans", method = RequestMethod.GET)
    public JSONObject getSpans(@RequestParam(required = false) String clusterId,
                               @RequestParam(required = false) String appId,
                               @RequestParam(required = false) String traceId,
                               @RequestParam(required = false) Integer page,
                               @RequestParam(required = false) Integer pageSize) {
        try {
            return traceApiService.getSpans(clusterId, appId, traceId, page, pageSize);
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
        }
        return new JSONObject();
    }

    @RequestMapping(value = "/spanEvents", method = RequestMethod.GET)
    public JSONObject getSpanEvents(@RequestParam(required = true) String traceId) {
        try {
            return traceApiService.getSpanEvents(traceId);
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
        }
        return new JSONObject();
    }

}
