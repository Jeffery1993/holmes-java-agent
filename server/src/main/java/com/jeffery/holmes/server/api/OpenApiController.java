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
 * Open API for searching data.
 */
@RestController
@RequestMapping(value = "/api", produces = "application/json; charset=utf-8")
public class OpenApiController {

    private static final Logger LOGGER = LoggerFactory.getLogger(OpenApiController.class);

    @Autowired
    private OpenApiService openApiService;

    @RequestMapping(value = "/monitor", method = RequestMethod.GET)
    public JSONObject searchMonitorData(@RequestParam(required = true) String clusterId,
                                        @RequestParam(required = true) String appId,
                                        @RequestParam(required = true) String name,
                                        @RequestParam(required = false) String startTime,
                                        @RequestParam(required = false) String endTime) {
        try {
            return openApiService.searchMonitorData(clusterId, appId, name, startTime, endTime);
        } catch (Exception e) {
            LOGGER.error(e.getMessage());
        }
        return new JSONObject();
    }

    @RequestMapping(value = "/trace", method = RequestMethod.GET)
    public JSONObject searchTraceData(@RequestParam(required = true) String clusterId,
                                      @RequestParam(required = true) String appId,
                                      @RequestParam(required = true) String traceId) {
        try {
            return openApiService.searchTraceData(clusterId, appId, traceId);
        } catch (Exception e) {
            LOGGER.error(e.getMessage());
        }
        return new JSONObject();
    }

}
