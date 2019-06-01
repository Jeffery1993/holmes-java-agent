package com.jeffery.holmes.demo.caller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
public class CallerService {

    @Value("${server.port}")
    private int serverPort;

    private static final HttpClient HTTP_CLIENT = new DefaultHttpClient();

    public JSONObject getCars(Integer page, Integer pageSize) throws IOException {
        page = (page == null) ? 1 : page;
        pageSize = (pageSize == null) ? 10 : pageSize;
        HttpGet get = new HttpGet(String.format("http://localhost:%d/api/callee/cars?page=%d&pageSize=%d", serverPort, page, pageSize));
        HttpResponse response = HTTP_CLIENT.execute(get);
        return JSON.parseObject(EntityUtils.toString(response.getEntity()));
    }

}
