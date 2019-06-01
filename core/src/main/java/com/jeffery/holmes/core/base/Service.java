package com.jeffery.holmes.core.base;

/**
 * Interface for service, which can be started and stopped.
 */
public interface Service {

    /**
     * Start the service.
     */
    void start();

    /**
     * Stop the service.
     */
    void stop();

}
