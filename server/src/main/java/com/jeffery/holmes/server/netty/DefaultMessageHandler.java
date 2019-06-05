package com.jeffery.holmes.server.netty;

import com.alibaba.fastjson.JSON;
import com.jeffery.holmes.server.index.IndexWriterFactory;
import com.jeffery.holmes.server.message.Message;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import org.apache.lucene.index.IndexWriter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Default message handler that store message to lucene.
 */
public class DefaultMessageHandler extends SimpleChannelInboundHandler<Object> {

    private static final Logger LOGGER = LoggerFactory.getLogger(DefaultMessageHandler.class);
    private static final IndexWriter INDEX_WRITER = IndexWriterFactory.getIndexWriter();

    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, Object object) throws Exception {
        if (!(object instanceof Message)) {
            LOGGER.error("Error type for Message!");
        }
        Message message = (Message) object;
        LOGGER.debug("Message: " + JSON.toJSONString(message));
        if (INDEX_WRITER != null) {
            INDEX_WRITER.addDocument(message.toDocument());
            INDEX_WRITER.commit();
        }
    }

}
