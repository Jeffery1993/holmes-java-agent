package com.jeffery.holmes.core.base;

import com.jeffery.holmes.common.util.LoggerFactory;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.TimeUnit;
import java.util.logging.Logger;

/**
 * Abstract class for queue service.
 *
 * @param <T> type of entity stored in the queue
 */
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

    /**
     * Get the blocking queue.
     *
     * @return the blocking queue used
     */
    protected abstract BlockingQueue<T> getBlockingQueue();

    /**
     * Get the number of consumers.
     *
     * @return the number of consumers
     */
    protected abstract int getNumberOfConsumers();

    /**
     * Get the consumer name, which is used in thread name.
     *
     * @return the consumer name
     */
    protected abstract String getConsumerName();

    /**
     * Get the task to be executed by consumers.
     *
     * @return the task to be executed by consumers
     */
    protected abstract Runnable getConsumeTask();

}
