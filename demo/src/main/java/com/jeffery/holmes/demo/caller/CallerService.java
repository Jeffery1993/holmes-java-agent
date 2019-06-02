package com.jeffery.holmes.demo.caller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.io.IOException;

@Service
public class CallerService {

    @Value("${server.port}")
    private int serverPort;
    private CloseableHttpClient closeableHttpClient;

    @PostConstruct
    public void init() {
        closeableHttpClient = HttpClients.createDefault();
    }

    public JSONObject getCars(Integer page, Integer pageSize) throws IOException {
        page = (page == null) ? 1 : page;
        pageSize = (pageSize == null) ? 10 : pageSize;
        HttpGet httpGet = new HttpGet(String.format("http://localhost:%d/api/callee/cars?page=%d&pageSize=%d", serverPort, page, pageSize));
        HttpResponse httpResponse = closeableHttpClient.execute(httpGet);
        return JSON.parseObject(EntityUtils.toString(httpResponse.getEntity()));
    }

    @PreDestroy
    public void destroy() {
        if (closeableHttpClient != null) {
            try {
                closeableHttpClient.close();
            } catch (IOException e) {
            }
        }
    }

}
