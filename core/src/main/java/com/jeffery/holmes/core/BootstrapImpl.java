package com.jeffery.holmes.core;

import com.jeffery.holmes.common.util.LoggerFactory;
import com.jeffery.holmes.core.collect.CollectTask;
import com.jeffery.holmes.core.collect.DataQueueService;
import com.jeffery.holmes.core.collect.TraceQueueService;
import com.jeffery.holmes.core.plugin.httpclient.InternalHttpClientTransformer;
import com.jeffery.holmes.core.plugin.mysql.PreparedStatementTransformer;
import com.jeffery.holmes.core.plugin.url.StandardHostValveTransformer;
import com.jeffery.holmes.core.transformer.CompoundTransformer;
import com.jeffery.holmes.core.transformer.TransformerManager;
import com.jeffery.holmes.premain.bootstrap.Bootstrap;

import java.lang.instrument.Instrumentation;
import java.util.logging.Logger;

public class BootstrapImpl implements Bootstrap {

    private static final Logger LOGGER = LoggerFactory.getLogger(BootstrapImpl.class);

    @Override
    public void boot(String args, Instrumentation instrumentation) {
        LOGGER.info("Holmes agent starting...");

        // 增加字节码增强器
        TransformerManager.register(new StandardHostValveTransformer());
        TransformerManager.register(new InternalHttpClientTransformer());
        TransformerManager.register(new PreparedStatementTransformer());
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
