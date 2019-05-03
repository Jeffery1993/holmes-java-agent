package com.jeffery.holmes.core.collect;

import com.jeffery.holmes.core.consts.ConfigConsts;
import com.jeffery.holmes.core.util.ConfigManager;

import java.io.*;
import java.net.InetAddress;
import java.net.Socket;

public class SocketService {

    private static String[] serverIps;
    private static int serverPort;
    private static ConnectStatus connectStatus;
    private static SocketEntity socketEntity;

    private static void initConfig() {
        if (ConfigManager.getProperty(ConfigConsts.serverIps) != null) {
            serverIps = ConfigManager.getProperty(ConfigConsts.serverIps).split(",");
        }
        if (ConfigManager.getProperty(ConfigConsts.serverPort) != null) {
            try {
                serverPort = Integer.valueOf(ConfigManager.getProperty(ConfigConsts.serverPort));
            } catch (Exception e) {
                // ignore
            }
        }
    }

    public static SocketEntity getSocketEntity() {
        if (connectStatus == null) {
            initConfig();
            connectStatus = ConnectStatus.CONNECTING;
            Socket socket = getSocket(serverIps, serverPort);
            if (socket == null) {
                connectStatus = ConnectStatus.INCONNECTABLE;
            } else {
                try {
                    socketEntity = new SocketEntity(socket);
                } catch (IOException e) {
                    connectStatus = ConnectStatus.INCONNECTABLE;
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
