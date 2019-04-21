package com.jeffery.holmes.premain.bootstrap;

import java.lang.instrument.Instrumentation;

public interface Bootstrap {

    void boot(String args, Instrumentation instrumentation);

}
