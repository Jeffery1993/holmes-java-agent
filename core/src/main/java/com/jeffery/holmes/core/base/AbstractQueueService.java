package com.jeffery.holmes.core.base;

import com.jeffery.holmes.core.util.LoggerFactory;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.TimeUnit;
import java.util.logging.Logger;

public abstract class AbstractQueueService<T> implements Service {

    protected Logger logger = LoggerFactory.getLogger(this.getClass());

    protected BlockingQueue<T> queue = getBlockingQueue();
    protected Thread[] consumers = new Thread[getNumberOfConsumers()];

    @Override
    public void start() {
        for (int i = 0; i < getNumberOfConsumers(); i++) {
            consumers[i] = new Thread(getConsumeTask(), getConsumerName() + "-thread-" + i);
            consumers[i].start();
        }
    }

    @Override
    public void stop() {
        for (int i = 0; i < getNumberOfConsumers(); i++) {
            consumers[i].interrupt();
            try {
                consumers[i].join();
            } catch (InterruptedException e) {
                logger.warning("Failed to stop consumer-" + i);
            }
        }
    }

    public boolean offer(T item) {
        return queue.offer(item);
    }

    public T poll() {
        return queue.poll();
    }

    public boolean offer(T item, long timeout, TimeUnit unit) throws InterruptedException {
        return queue.offer(item, timeout, unit);
    }

    public T poll(long timeout, TimeUnit unit) throws InterruptedException {
        return queue.poll(timeout, unit);
    }

    public void put(T item) throws InterruptedException {
        queue.put(item);
    }

    public T take() throws InterruptedException {
        return queue.take();
    }

    protected abstract BlockingQueue<T> getBlockingQueue();

    protected abstract int getNumberOfConsumers();

    protected abstract String getConsumerName();

    protected abstract Runnable getConsumeTask();

}
