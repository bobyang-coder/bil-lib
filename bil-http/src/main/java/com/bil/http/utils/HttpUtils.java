package com.bil.http.utils;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletRequest;
import java.net.InetAddress;
import java.net.UnknownHostException;

/**
 * http工具类
 *
 * @author haibo.yang   <bobyang_coder@163.com>
 * @since 2020/6/7
 */
public class HttpUtils {
    private static final Logger log = LoggerFactory.getLogger(HttpUtils.class);

    /**
     * 本地ip地址
     */
    public static final String LOCAL_IP = "127.0.0.1";
    /**
     * 默认ip地址
     */
    public static final String DEFAULT_IP = "0:0:0:0:0:0:0:1";
    /**
     * 默认ip地址长度
     */
    public static final int DEFAULT_IP_LENGTH = 15;

    /**
     * 检测是否https
     *
     * @param url URL
     * @return 是否https
     */
    public static boolean isHttps(String url) {
        return url.toLowerCase().startsWith("https");
    }


    /**
     * 判断url是否有http前缀
     *
     * @param url
     * @return
     */
    public static boolean hasHttpPrefix(String url) {
        return null != url && (url.startsWith("http://") || url.startsWith("https://"));
    }

    /**
     * 获取合法ip地址
     *
     * @param request
     * @return
     */
    public static String getRealIpAddress(HttpServletRequest request) {
        //squid 服务代理
        String ip = request.getHeader("x-forwarded-for");
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            //apache服务代理
            ip = request.getHeader("Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            //weblogic 代理
            ip = request.getHeader("WL-Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            //有些代理
            ip = request.getHeader("HTTP_CLIENT_IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            //nginx代理
            ip = request.getHeader("X-Real-IP");
        }
        /*
         * 如果此时还是获取不到ip地址，那么最后就使用request.getRemoteAddr()来获取
         * */
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
            if (StringUtils.equals(ip, LOCAL_IP) || StringUtils.equals(ip, DEFAULT_IP)) {
                //根据网卡取本机配置的IP
                InetAddress iNet = null;
                try {
                    iNet = InetAddress.getLocalHost();
                    ip = iNet.getHostAddress();
                } catch (UnknownHostException e) {
                    log.error("InetAddress getLocalHost error In HttpUtils getRealIpAddress: ", e);
                }
            }
        }
        //对于通过多个代理的情况，第一个IP为客户端真实IP,多个IP按照','分割
        //"***.***.***.***".length() = 15
        if (!StringUtils.isEmpty(ip) && ip.length() > DEFAULT_IP_LENGTH) {
            if (ip.indexOf(",") > 0) {
                ip = ip.substring(0, ip.indexOf(","));
            }
        }
        return ip;
    }
}
