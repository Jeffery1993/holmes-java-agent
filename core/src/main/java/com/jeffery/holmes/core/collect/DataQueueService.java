package com.jeffery.holmes.core.collect;

import com.jeffery.holmes.core.base.AbstractQueueService;
import com.jeffery.holmes.core.consts.DataCategoryEnum;
import com.jeffery.holmes.core.message.Message;
import com.jeffery.holmes.core.message.MessageImpl;

import java.util.Map;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

/**
 * Queue service for monitor data.
 */
public class DataQueueService extends AbstractQueueService<Map<String, Object>> {

    private static final DataQueueService DATA_QUEUE_SERVICE = new DataQueueService();
    private static final int DATA_QUEUE_SIZE = 512;

    private DataQueueService() {
    }

    public static DataQueueService getInstance() {
        return DATA_QUEUE_SERVICE;
    }

    @Override
    protected BlockingQueue<Map<String, Object>> getBlockingQueue() {
        return new ArrayBlockingQueue<Map<String, Object>>(DATA_QUEUE_SIZE);
    }

    @Override
    protected int getNumberOfConsumers() {
        return 1;
    }

    @Override
    protected String getConsumerName() {
        return "data-consumer";
    }

    @Override
    protected Runnable getConsumeTask() {
        return new Runnable() {
            @Override
            public void run() {
                while (true) {
                    try {
                        Map<String, Object> item = queue.take();
                        Message message = new MessageImpl(DataCategoryEnum.MONITOR.getCode(), item);
                        message.addHeader("name", item.get("name"));
                        message.addHeader("timestamp", item.get("timestamp"));
                        TransferService.transfer(message);
                    } catch (InterruptedException e) {
                        return;
                    }
                }
            }
        };
    }

}
