package com.jeffery.holmes.core;

import com.jeffery.holmes.core.collect.CollectTask;
import com.jeffery.holmes.core.collect.DataQueueService;
import com.jeffery.holmes.core.collect.TraceQueueService;
import com.jeffery.holmes.core.transformer.CompoundTransformer;
import com.jeffery.holmes.core.util.ConfigManager;
import com.jeffery.holmes.core.util.LoggerFactory;
import com.jeffery.holmes.premain.bootstrap.Bootstrap;

import java.lang.instrument.Instrumentation;
import java.util.logging.Logger;

public class BootstrapImpl implements Bootstrap {

    private static final Logger LOGGER = LoggerFactory.getLogger(BootstrapImpl.class);

    @Override
    public void boot(String args, Instrumentation instrumentation) {
        LOGGER.info("Holmes agent starting...");

        // 初始化配置
        ConfigManager.init(args);

        // 增加字节码增强器
        instrumentation.addTransformer(new CompoundTransformer(), true);

        // 启动任务
        CollectTask.getInstance().execute();
        DataQueueService.getInstance().start();
        TraceQueueService.getInstance().start();

        // 添加关闭钩子
        Runtime.getRuntime().addShutdownHook(new Thread(new Runnable() {
            @Override
            public void run() {
                stopAgent();
            }
        }));

        LOGGER.info("Holmes agent started!!!");
    }

    private void stopAgent() {
        // 停止任务
        CollectTask.getInstance().shutdown();
        DataQueueService.getInstance().stop();
        TraceQueueService.getInstance().stop();
    }

}
