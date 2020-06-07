package com.bil.lang.proxy;

import sun.misc.ProxyGenerator;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * 代理技术相关工具类
 *
 * @author haibo.yang   <bobyang_coder@163.com>
 * @since 2018/1/25
 */
public class ProxyUtil {

    /**
     * 根据动态代理生成的bean对象，获取对象的字节码文件
     *
     * @param bean      动态代理生成的bean对象
     * @param targetDir 生成class文件存储目录
     * @throws IOException
     */
    public static void convertBeanToClassFile(Object bean, String targetDir) throws IOException {
        Class<?> aClass = bean.getClass();
        byte[] proxyClass = ProxyGenerator.generateProxyClass(aClass.getSimpleName(), aClass.getInterfaces());
        //将字节码文件保存到targetDir文件夹，文件名为$Proxy0.class
        FileOutputStream outputStream = new FileOutputStream(new File(targetDir + "$Proxy0.class"));
        outputStream.write(proxyClass);
        outputStream.flush();
        outputStream.close();
    }
}
