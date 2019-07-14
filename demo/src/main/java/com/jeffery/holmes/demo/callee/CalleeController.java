package com.jeffery.holmes.demo.callee;

import com.alibaba.fastjson.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/api/callee", produces = "application/json; charset=utf-8")
public class CalleeController {

    @Autowired
    private CalleeService calleeService;

    @RequestMapping(value = "/cars", method = RequestMethod.GET)
    public JSONObject getCars(@RequestParam(required = false) Integer page, @RequestParam(required = false) Integer pageSize) throws Exception {
        JSONObject res = new JSONObject();
        res.put("cars", calleeService.getCars(page, pageSize));
        return res;
    }

}
