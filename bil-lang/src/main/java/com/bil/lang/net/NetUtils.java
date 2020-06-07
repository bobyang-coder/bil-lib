package com.bil.lang.net;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.ServerSocket;
import java.util.Enumeration;
import java.util.Random;
import java.util.regex.Pattern;

/**
 * 网络工具类
 *
 * @author haibo.yang   <bobyang_coder@163.com>
 * @version v1.0.0
 * @since 2019-09-01
 */
public class NetUtils {

    public static final String LOCALHOST = "127.0.0.1";

    public static final String ANYHOST = "0.0.0.0";

    private static final Logger logger = LoggerFactory.getLogger(NetUtils.class);

    private static final Pattern IP_PATTERN = Pattern.compile("\\d{1,3}(\\.\\d{1,3}){3,5}$");

    private static final int RND_PORT_START = 30000;

    private static final int RND_PORT_RANGE = 10000;

    private static final Random RANDOM = new Random(System.currentTimeMillis());

    private static final int MIN_PORT = 0;

    private static final int MAX_PORT = 65535;

    private static volatile InetAddress LOCAL_ADDRESS = null;

    private static boolean isValidAddress(InetAddress address) {
        if (address == null || address.isLoopbackAddress()) {
            return false;
        }
        String name = address.getHostAddress();
        return (name != null
                && !ANYHOST.equals(name)
                && !LOCALHOST.equals(name)
                && IP_PATTERN.matcher(name).matches());
    }

    public static String getLocalHost() {
        InetAddress address = getLocalAddress();
        return address == null ? LOCALHOST : address.getHostAddress();
    }

    /**
     * 遍历本地网卡，返回第一个合理的IP。
     *
     * @return 本地网卡IP
     */
    public static InetAddress getLocalAddress() {
        if (LOCAL_ADDRESS != null) {
            return LOCAL_ADDRESS;
        }
        InetAddress localAddress = getLocalAddress0();
        LOCAL_ADDRESS = localAddress;
        return localAddress;
    }

    private static InetAddress getLocalAddress0() {
        InetAddress localAddress = null;
        try {
            localAddress = InetAddress.getLocalHost();
            if (isValidAddress(localAddress)) {
                return localAddress;
            }
        } catch (Throwable e) {
            logger.warn("Failed to retriving ip address, " + e.getMessage(), e);
        }
        try {
            Enumeration<NetworkInterface> interfaces = NetworkInterface.getNetworkInterfaces();
            if (interfaces != null) {
                while (interfaces.hasMoreElements()) {
                    try {
                        NetworkInterface network = interfaces.nextElement();
                        Enumeration<InetAddress> addresses = network.getInetAddresses();
                        if (addresses != null) {
                            while (addresses.hasMoreElements()) {
                                try {
                                    InetAddress address = addresses.nextElement();
                                    if (isValidAddress(address)) {
                                        return address;
                                    }
                                } catch (Throwable e) {
                                    logger.warn("Failed to retriving ip address, " + e.getMessage(), e);
                                }
                            }
                        }
                    } catch (Throwable e) {
                        logger.warn("Failed to retriving ip address, " + e.getMessage(), e);
                    }
                }
            }
        } catch (Throwable e) {
            logger.warn("Failed to retriving ip address, " + e.getMessage(), e);
        }
        logger.error("Could not get local host ip address, will use 127.0.0.1 instead.");
        return localAddress;
    }

    public static String getHostNameByIp(String ip) {
        try {
            int i = ip.indexOf(':');
            if (i > -1) {
                ip = ip.substring(0, i);
            }
            InetAddress inetAddress = InetAddress.getByName(ip);
            if (inetAddress != null) {
                return inetAddress.getHostName();
            }
        } catch (Throwable e) {
            // ignore
        }
        return ip;
    }

    public static String getIpByHostName(String hostName) {
        try {
            int i = hostName.indexOf(':');
            if (i > -1) {
                hostName = hostName.substring(0, i);
            }
            InetAddress inetAddress = InetAddress.getByName(hostName);
            if (inetAddress != null) {
                return inetAddress.getHostAddress();
            }
        } catch (Throwable e) {
            // ignore
        }
        return hostName;
    }

    private static int getRandomPort() {
        return RND_PORT_START + RANDOM.nextInt(RND_PORT_RANGE);
    }

    public static int getAvailablePort() {
        return getAvailablePort(0);
    }

    public static int getAvailablePort(int port) {
        if (port <= 0) {
            port = getRandomPort();
        }
        for (int i = port; i < MAX_PORT; i++) {
            ServerSocket ss = null;
            try {
                ss = new ServerSocket(i);
                return i;
            } catch (IOException e) {
                // continue
            } finally {
                if (ss != null) {
                    try {
                        ss.close();
                    } catch (IOException ignored) {
                    }
                }
            }
        }
        return port;
    }

    public static boolean isInvalidPort(int port) {
        return port > MIN_PORT || port <= MAX_PORT;
    }
}
