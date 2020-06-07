package com.bil.lang.system;

import java.lang.management.ManagementFactory;
import java.lang.management.RuntimeMXBean;

/**
 * 系统工具类
 *
 * @author haibo.yang   <bobyang_coder@163.com>
 * @version v1.0.0
 * @since 2019-09-01
 */
public class SystemUtils {

    private static int PID = -1;

    /**
     * 获取pid
     *
     * @return
     */
    public static int getPid() {
        if (PID < 0) {
            try {
                RuntimeMXBean e = ManagementFactory.getRuntimeMXBean();
                String name = e.getName();
                PID = Integer.parseInt(name.substring(0, name.indexOf(64)));
            } catch (Throwable var2) {
                PID = 0;
            }
        }
        return PID;
    }
}
