package com.jeffery.holmes.server.netty;

import com.jeffery.holmes.server.message.Message;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;
import io.netty.util.CharsetUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

/**
 * Default message decoder that interpret data of byte array to readable messages.
 */
public class DefaultMessageDecoder extends ByteToMessageDecoder {

    private static final Logger LOGGER = LoggerFactory.getLogger(DefaultMessageDecoder.class);
    private static final int MAGIC_NUMBER = 6666;
    private static final int HEAD_SIZE = 13;
    private static final int MAX_SIZE = 5 * (1 << 20);

    @Override
    protected void decode(ChannelHandlerContext channelHandlerContext, ByteBuf byteBuf, List<Object> list) throws Exception {
        if (byteBuf.readableBytes() < HEAD_SIZE) {
            return;
        }
        byteBuf.markReaderIndex();
        int magicNumber = byteBuf.readInt();
        if (magicNumber != MAGIC_NUMBER) {
            LOGGER.error("Error magic number " + magicNumber);
            return;
        }
        byte type = byteBuf.readByte();
        int lengthOfHeaders = byteBuf.readInt();
        int lengthOfBody = byteBuf.readInt();
        if (byteBuf.readableBytes() < lengthOfHeaders + lengthOfBody) {
            byteBuf.resetReaderIndex();
            return;
        }
        if (HEAD_SIZE + lengthOfHeaders + lengthOfBody > MAX_SIZE) {
            LOGGER.error("Message is too large!");
            return;
        }
        String headers = byteBuf.readBytes(lengthOfHeaders).toString(CharsetUtil.UTF_8);
        String body = byteBuf.readBytes(lengthOfBody).toString(CharsetUtil.UTF_8);

        Message message = new Message();
        message.setType(type);
        message.setHeaders(headers);
        message.setBody(body);
        list.add(message);
    }

}
