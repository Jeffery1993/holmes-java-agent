package com.jeffery.holmes.core.collect;

import com.alibaba.fastjson.JSON;
import com.jeffery.holmes.common.util.LoggerFactory;
import com.jeffery.holmes.core.message.Message;

import java.io.DataOutputStream;
import java.io.IOException;
import java.util.logging.Logger;

/**
 * Service to transfer data based on TCP protocol.
 */
public class TransferService {

    private static final Logger LOGGER = LoggerFactory.getLogger(TransferService.class);

    private static final int MAGIC_NUMBER = 6666;
    private static final int DEFAULT_RETRY_TIMES = 2;
    private static DataOutputStream dataOutputStream;

    static {
        SocketService.SocketEntity socketEntity = SocketService.getSocketEntity();
        if (socketEntity != null) {
            dataOutputStream = socketEntity.getDataOutputStream();
        }
    }

    public static void transfer(Message message) {
        transfer(message, DEFAULT_RETRY_TIMES);
    }

    public static void transfer(Message message, int retryTimes) {
        LOGGER.info(JSON.toJSONString(message));
        for (int i = 0; i < retryTimes; i++) {
            try {
                if (doTransfer(message)) {
                    break;
                }
            } catch (Exception e) {
                // ignore
            }
        }
    }

    private static boolean doTransfer(Message message) throws IOException {
        if (dataOutputStream != null) {
            dataOutputStream.writeInt(MAGIC_NUMBER);
            dataOutputStream.writeByte(message.getType());
            byte[] headers = message.getHeadersAsBytes();
            byte[] body = message.getBodyAsBytes();
            dataOutputStream.writeInt(headers.length);
            dataOutputStream.writeInt(body.length);
            dataOutputStream.write(headers);
            dataOutputStream.write(body);
            dataOutputStream.flush();
            return true;
        }
        return false;
    }

}
