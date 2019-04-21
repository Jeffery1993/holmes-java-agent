package com.jeffery.holmes.core.base;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public abstract class QueueService<T> implements Service {

    private BlockingQueue<T> queue;

    public void put(T item) throws InterruptedException {
        queue.put(item);
    }

    public T take() throws InterruptedException {
        return queue.take();
    }

    @Override
    public void start() {
        queue = new LinkedBlockingQueue<T>(getQueueSize());
    }

    @Override
    public void stop() {
        queue.clear();
    }

    protected abstract int getQueueSize();

}
