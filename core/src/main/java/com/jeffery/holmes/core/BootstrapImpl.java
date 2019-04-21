package com.jeffery.holmes.core;

import com.jeffery.holmes.common.util.ConfigManager;
import com.jeffery.holmes.common.util.LoggerFactory;
import com.jeffery.holmes.core.transformer.CompoundTransformer;
import com.jeffery.holmes.premain.bootstrap.Bootstrap;

import java.lang.instrument.Instrumentation;
import java.util.logging.Logger;

public class BootstrapImpl implements Bootstrap {

    private static final Logger LOGGER = LoggerFactory.getLogger(BootstrapImpl.class);

    @Override
    public void boot(Instrumentation instrumentation) {
        LOGGER.info("Holmes agent starting...");

        // 初始化配置
        ConfigManager.init();

        // 增加字节码增强器
        instrumentation.addTransformer(new CompoundTransformer(), true);

        LOGGER.info("Holmes agent started!!!");
    }

}
