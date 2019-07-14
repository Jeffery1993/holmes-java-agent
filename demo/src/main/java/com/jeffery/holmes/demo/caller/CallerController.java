package com.jeffery.holmes.demo.caller;

import com.alibaba.fastjson.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@RestController
@RequestMapping(value = "/api/caller", produces = "application/json; charset=utf-8")
public class CallerController {

    @Autowired
    private CallerService callerService;

    @RequestMapping(value = "/cars", method = RequestMethod.GET)
    public JSONObject getCars(@RequestParam(required = false) Integer page, @RequestParam(required = false) Integer pageSize) throws IOException {
        return callerService.getCars(page, pageSize);
    }

}
