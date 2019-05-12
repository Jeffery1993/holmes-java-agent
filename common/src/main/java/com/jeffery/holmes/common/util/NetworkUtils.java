package com.jeffery.holmes.common.util;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.util.Enumeration;

public class NetworkUtils {

    private static final String WINDOWS = "windows";
    private static final String OS_NAME = "os.name";

    public static boolean isWindowsOS() {
        String osName = System.getProperty(OS_NAME);
        return osName.toLowerCase().contains(WINDOWS);
    }

    public static String getIpAddress() {
        InetAddress ip = null;
        try {
            if (isWindowsOS()) {
                ip = InetAddress.getLocalHost();
            } else {
                if (!InetAddress.getLocalHost().isLoopbackAddress()) {
                    ip = InetAddress.getLocalHost();
                } else {
                    boolean found = false;
                    Enumeration<NetworkInterface> netInterfaces = NetworkInterface.getNetworkInterfaces();
                    while (netInterfaces.hasMoreElements()) {
                        if (found) {
                            break;
                        }
                        NetworkInterface ni = netInterfaces.nextElement();
                        Enumeration<InetAddress> ips = ni.getInetAddresses();
                        while (ips.hasMoreElements()) {
                            ip = ips.nextElement();
                            if (ip.isSiteLocalAddress() && !ip.isLoopbackAddress() && !ip.getHostAddress().contains(":")) {
                                found = true;
                                break;
                            }
                        }

                    }
                }
            }
        } catch (Exception e) {
        }

        return ip == null ? null : ip.getHostAddress();
    }

}
