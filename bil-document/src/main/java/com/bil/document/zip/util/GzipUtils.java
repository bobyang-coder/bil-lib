package com.bil.document.zip.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

/**
 * gzip压缩
 *
 * @author haibo.yang   <bobyang_coder@163.com>
 * @since 2020/08/12
 */
public final class GzipUtils {
    private static final Logger logger = LoggerFactory.getLogger(GzipUtils.class);
    public static final int BUFFER = 1024;

    public static byte[] compress(byte[] data) {
        try (ByteArrayInputStream bais = new ByteArrayInputStream(data);
             ByteArrayOutputStream baos = new ByteArrayOutputStream()) {
            compress(bais, baos);
            return baos.toByteArray();
        } catch (IOException var7) {
            logger.error("压缩失败", var7);
        }
        return null;
    }

    public static byte[] decompress(byte[] data) {
        try (ByteArrayInputStream bais = new ByteArrayInputStream(data);
             ByteArrayOutputStream baos = new ByteArrayOutputStream()) {
            decompress(bais, baos);
            return baos.toByteArray();
        } catch (IOException var7) {
            logger.error("解压失败", var7);
        }
        return new byte[0];
    }

    public static void compress(InputStream is, OutputStream os) throws IOException {
        try (GZIPOutputStream gos = new GZIPOutputStream(os)) {
            byte[] data = new byte[BUFFER];
            int count;
            while ((count = is.read(data, 0, BUFFER)) != -1) {
                gos.write(data, 0, count);
            }
        }
    }

    public static void decompress(InputStream is, OutputStream os) throws IOException {
        try (GZIPInputStream gis = new GZIPInputStream(is)) {
            byte[] data = new byte[BUFFER];
            int count;
            while ((count = gis.read(data, 0, BUFFER)) != -1) {
                os.write(data, 0, count);
            }
        }
    }
}
