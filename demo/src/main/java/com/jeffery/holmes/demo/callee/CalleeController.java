package com.jeffery.holmes.demo.callee;

import com.alibaba.fastjson.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/api/callee", produces = "application/json; charset=utf-8")
public class CalleeController {

    private static final Logger LOGGER = LoggerFactory.getLogger(CalleeController.class);

    @Autowired
    private CalleeService calleeService;

    @RequestMapping(value = "/cars", method = RequestMethod.GET)
    public JSONObject getCars(@RequestParam(required = false) Integer page, @RequestParam(required = false) Integer pageSize) {
        JSONObject res = new JSONObject();
        try {
            res.put("cars", calleeService.getCars(page, pageSize));
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
            res.put("error", e.getMessage());
        }
        return res;
    }

}
