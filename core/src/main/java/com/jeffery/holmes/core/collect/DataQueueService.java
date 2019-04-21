package com.jeffery.holmes.core.collect;

import com.jeffery.holmes.core.base.QueueService;
import com.jeffery.holmes.core.message.Message;

public class DataQueueService extends QueueService<Message> {

    @Override
    protected int getQueueSize() {
        return 512;
    }

}
