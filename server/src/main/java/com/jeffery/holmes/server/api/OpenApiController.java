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
 * Open API for searching data.
 */
@RestController
@RequestMapping(value = "/api", produces = "application/json; charset=utf-8")
public class OpenApiController {

    private static final Logger LOGGER = LoggerFactory.getLogger(OpenApiController.class);

    @Autowired
    private OpenApiService openApiService;

    @RequestMapping(value = "/monitor", method = RequestMethod.GET)
    public JSONObject searchMonitorData(@RequestParam(required = false) String clusterId,
                                        @RequestParam(required = false) String appId,
                                        @RequestParam(required = false) CollectorEnum name,
                                        @RequestParam(required = false) String startTime,
                                        @RequestParam(required = false) String endTime,
                                        @RequestParam(required = false) boolean pretty) {
        try {
            return openApiService.searchMonitorData(clusterId, appId, name, startTime, endTime, pretty);
        } catch (Exception e) {
            LOGGER.error(e.getMessage());
        }
        return new JSONObject();
    }

    @RequestMapping(value = "/trace", method = RequestMethod.GET)
    public JSONObject searchTraceData(@RequestParam(required = false) String clusterId,
                                      @RequestParam(required = false) String appId,
                                      @RequestParam(required = false) String traceId,
                                      @RequestParam(required = false) boolean pretty) {
        try {
            return openApiService.searchTraceData(clusterId, appId, traceId, pretty);
        } catch (Exception e) {
            LOGGER.error(e.getMessage());
        }
        return new JSONObject();
    }

}
