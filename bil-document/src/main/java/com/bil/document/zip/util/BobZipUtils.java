package com.bil.document.zip.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.util.zip.Adler32;
import java.util.zip.CheckedOutputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

/**
 * zip压缩、解压缩工具类
 *
 * @author bob   <bobyang_coder@163.com>
 * @version v1.0
 * @since 2017/9/27
 */
public class BobZipUtils {

    private static final Logger logger = LoggerFactory.getLogger(BobZipUtils.class);
    private static final int BUFFER = 1024 * 10;

    /**
     * 将指定目录压缩到和该目录同名的zip文件，自定义压缩路径
     *
     * @param sourceFilePath 目标文件路径
     * @param zipFilePath    指定zip文件路径
     * @return
     */
    public static boolean zip(String sourceFilePath, String zipFilePath) {
        boolean result = false;
        File source = new File(sourceFilePath);
        if (!source.exists()) {
            logger.info("[目标文件路径不存在]");
            return result;
        }
        File zipFile = new File(zipFilePath + "/" + source.getName() + ".zip");
        if (zipFile.exists()) {
            logger.info("[zip文件已存在]");
            return result;
        } else {
            if (!zipFile.getParentFile().exists()) {
                if (!zipFile.getParentFile().mkdirs()) {
                    logger.info("[zip文件创建失败]");
                    return result;
                }
            }
        }
        logger.info("[压缩文件开始......]");
        FileOutputStream dest = null;
        ZipOutputStream out = null;
        try {
            dest = new FileOutputStream(zipFile);
            CheckedOutputStream checksum = new CheckedOutputStream(dest, new Adler32());
            out = new ZipOutputStream(new BufferedOutputStream(checksum));
            out.setMethod(ZipOutputStream.DEFLATED);
            compress(source, out);
            out.flush();
            result = true;
        } catch (FileNotFoundException e) {
            logger.error("[文件不存在]", e);
        } catch (IOException e) {
            logger.error("[文件写入异常]", e);
        } finally {
            if (out != null) {
                try {
                    out.closeEntry();
                } catch (IOException e) {
                    logger.info("[流关闭失败]", e);
                }
                close(out);
            }
            close(dest);
        }
        logger.info("[压缩文件结束......]");
        return result;
    }

    /**
     * 压缩文件
     *
     * @param file 文件、文件夹
     * @param out  输出流
     */
    private static void compress(File file, ZipOutputStream out) {
        if (file.isFile()) {//单个文件
            FileInputStream fi = null;
            BufferedInputStream origin = null;
            try {
                fi = new FileInputStream(file);
                origin = new BufferedInputStream(fi, BUFFER);
                int index = file.getAbsolutePath().lastIndexOf(File.separator);
                String entryName = file.getAbsolutePath().substring(index);
                logger.info("[被压缩文件名称-entryName:{}]", entryName);
                ZipEntry entry = new ZipEntry(entryName);
                out.putNextEntry(entry);
                byte[] data = new byte[BUFFER];
                int count;
                while ((count = origin.read(data, 0, BUFFER)) != -1) {
                    out.write(data, 0, count);
                }
            } catch (FileNotFoundException e) {
                logger.error("[文件不存在]", e);
            } catch (IOException e) {
                logger.error("[读取文件异常]", e);
            } finally {
                close(origin);
                close(fi);
            }
        } else {//目录
            File[] fs = file.listFiles();
            if (fs != null && fs.length > 0) {
                for (File f : fs) {
                    compress(f, out);
                }
            }
        }
    }

    /**
     * 关闭流
     *
     * @param t
     * @param <T>
     */
    public static <T extends Closeable> void close(T t) {
        if (t != null) {
            try {
                t.close();
            } catch (IOException e) {
                logger.error("[流关闭失败]", e);
            }
        }
    }

}
