package com.jeffery.holmes.agent.bootstrap;

import java.lang.instrument.Instrumentation;

/**
 * Interface for agent boot class.
 */
public interface Bootstrap {

    /**
     * Start the agent.
     *
     * @param args            arguments provided to the agent
     * @param instrumentation reference of instrumentation
     */
    void boot(String args, Instrumentation instrumentation);

}
