package com.jeffery.holmes.core.collect;

import com.jeffery.holmes.common.util.ConfigManager;
import com.jeffery.holmes.common.util.LoggerFactory;

import java.io.*;
import java.net.InetAddress;
import java.net.Socket;
import java.util.Arrays;
import java.util.logging.Logger;

/**
 * Service to get socket entity.
 */
public class SocketService {

    private static final Logger LOGGER = LoggerFactory.getLogger(SocketService.class);

    private static String[] serverIps = ConfigManager.getServerIps();
    private static int serverPort = ConfigManager.getServerPort();
    private static ConnectStatus connectStatus;
    private static SocketEntity socketEntity;

    public static SocketEntity getSocketEntity() {
        if (connectStatus == null) {
            connectStatus = ConnectStatus.CONNECTING;
            Socket socket = getSocket(serverIps, serverPort);
            if (socket == null) {
                connectStatus = ConnectStatus.INCONNECTABLE;
            } else {
                try {
                    socketEntity = new SocketEntity(socket);
                } catch (IOException e) {
                    connectStatus = ConnectStatus.INCONNECTABLE;
                    LOGGER.severe("Failed to connect to ips " + Arrays.toString(serverIps) + " port " + serverPort + " due to " + e);
                }
                connectStatus = ConnectStatus.CONNECTED;
            }
        }
        return socketEntity;
    }

    public static Socket getSocket(String[] ips, int port) {
        Socket socket = null;
        for (String ip : ips) {
            try {
                socket = getSocket(ip, port);
            } catch (Exception e) {
                // ignore
            }
            if (socket != null) {
                LOGGER.info("Connected to ip " + ip + " port " + serverPort);
                break;
            }
        }
        return socket;
    }

    public static Socket getSocket(String ip, int port) throws IOException {
        InetAddress inetAddress = InetAddress.getByName(ip);
        Socket socket = new Socket(inetAddress, port);
        socket.setKeepAlive(true);
        socket.setSoTimeout(3000);
        return socket;
    }

    enum ConnectStatus {
        CONNECTING, CONNECTED, INCONNECTABLE;
    }

    public static class SocketEntity {

        private Socket socket;
        private DataInputStream dataInputStream;
        private DataOutputStream dataOutputStream;

        public SocketEntity(Socket socket) throws IOException {
            this.socket = socket;
            this.dataInputStream = new DataInputStream(new BufferedInputStream(socket.getInputStream()));
            this.dataOutputStream = new DataOutputStream(new BufferedOutputStream(socket.getOutputStream()));
        }

        public Socket getSocket() {
            return socket;
        }

        public void setSocket(Socket socket) {
            this.socket = socket;
        }

        public DataInputStream getDataInputStream() {
            return dataInputStream;
        }

        public void setDataInputStream(DataInputStream dataInputStream) {
            this.dataInputStream = dataInputStream;
        }

        public DataOutputStream getDataOutputStream() {
            return dataOutputStream;
        }

        public void setDataOutputStream(DataOutputStream dataOutputStream) {
            this.dataOutputStream = dataOutputStream;
        }

    }

}
