package com.jeffery.holmes.demo.callee;

import com.alibaba.fastjson.JSONArray;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CalleeService {

    @Autowired
    private CalleeDao calleeDao;

    public JSONArray getCars(Integer page, Integer pageSize) throws Exception {
        return calleeDao.getCars(page, pageSize);
    }

}
