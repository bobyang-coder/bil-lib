package com.bil.http.utils;

/**
 * http工具类
 *
 * @author haibo.yang   <bobyang_coder@163.com>
 * @since 2020/6/7
 */
public class HttpUtils {

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

}
