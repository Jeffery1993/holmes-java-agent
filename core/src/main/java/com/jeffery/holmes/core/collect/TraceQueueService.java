package com.jeffery.holmes.core.collect;

import com.jeffery.holmes.common.trace.Span;
import com.jeffery.holmes.common.trace.TraceCollector;
import com.jeffery.holmes.core.base.AbstractQueueService;
import com.jeffery.holmes.core.consts.DataCategoryEnum;
import com.jeffery.holmes.core.message.Message;
import com.jeffery.holmes.core.message.MessageImpl;

import java.util.concurrent.BlockingQueue;

public class TraceQueueService extends AbstractQueueService<Object> {

    private static final TraceQueueService TRACE_QUEUE_SERVICE = new TraceQueueService();

    private TraceQueueService() {
    }

    public static TraceQueueService getInstance() {
        return TRACE_QUEUE_SERVICE;
    }

    @Override
    protected BlockingQueue<Object> getBlockingQueue() {
        return TraceCollector.TRACE_BLOCKING_QUEUE;
    }

    @Override
    protected int getNumberOfConsumers() {
        return 2;
    }

    @Override
    protected String getConsumerName() {
        return "trace-consumer";
    }

    @Override
    protected Runnable getConsumeTask() {
        return new Runnable() {
            @Override
            public void run() {
                while (true) {
                    try {
                        Object item = queue.take();
                        if (item instanceof Span) {
                            Message message = new MessageImpl(DataCategoryEnum.SPAN.getCode(), item);
                            TransferService.transfer(message);
                        } else {
                            Message message = new MessageImpl(DataCategoryEnum.SPAN_EVENT.getCode(), item);
                            TransferService.transfer(message);
                        }
                    } catch (InterruptedException e) {
                        // ignore
                    }
                }
            }
        };
    }

}
