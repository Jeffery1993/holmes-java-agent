package com.jeffery.holmes.server.stream;

import com.jeffery.holmes.server.view.View;
import com.jeffery.holmes.server.view.ViewConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;

/**
 * A stream of collector data.
 */
public class CollectorDataStream {

    private static final Logger LOGGER = LoggerFactory.getLogger(CollectorDataStream.class);

    private static final ExecutorService EXECUTOR_SERVICE = Executors.newFixedThreadPool(10);

    private final List<CollectorData> collectorDataList;
    private List<ViewConfig> viewConfigs;
    private List<Future<View>> futureList;

    private CollectorDataStream(List<CollectorData> collectorDataList) {
        this.collectorDataList = collectorDataList;
    }

    public static CollectorDataStream of(List<CollectorData> collectorDataList) {
        if (CollectionUtils.isEmpty(collectorDataList)) {
            throw new IllegalArgumentException("Empty collector data!");
        } else if (collectorDataList.size() > 1000) {
            throw new IllegalArgumentException("Too many dots!");
        }
        return new CollectorDataStream(collectorDataList);
    }

    public CollectorDataStream setViewConfigs(List<ViewConfig> viewConfigs) {
        if (CollectionUtils.isEmpty(viewConfigs)) {
            throw new IllegalArgumentException("Invalid view configs!");
        }
        this.viewConfigs = viewConfigs;
        return this;
    }

    public CollectorDataStream compute() {
        futureList = new ArrayList<>();
        for (ViewConfig viewConfig : viewConfigs) {
            futureList.add(EXECUTOR_SERVICE.submit(new ComputeTask(viewConfig, collectorDataList)));
        }
        return this;
    }

    public List<View> toViewList() {
        List<View> views = new ArrayList<>();
        for (Future<View> future : futureList) {
            try {
                views.add(future.get());
            } catch (InterruptedException | ExecutionException e) {
                LOGGER.error(e.getMessage(), e);
            }
        }
        return views;
    }

    static class ComputeTask implements Callable<View> {

        private ViewConfig viewConfig;
        private List<CollectorData> collectorDataList;

        public ComputeTask(ViewConfig viewConfig, List<CollectorData> collectorDataList) {
            this.viewConfig = viewConfig;
            this.collectorDataList = collectorDataList;
        }

        @Override
        public View call() throws Exception {
            return viewConfig.compute(collectorDataList);
        }

    }

}
