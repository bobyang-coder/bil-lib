package com.bil.document.zip.util;

import org.apache.commons.io.IOUtils;
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
public final class GzipUtil {
    private static final Logger logger = LoggerFactory.getLogger(GzipUtil.class);
    public static final int BUFFER = 1024;

    public static byte[] compress(byte[] data) {
        ByteArrayInputStream bais = new ByteArrayInputStream(data);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        try {
            compress(bais, baos);
            return baos.toByteArray();
        } catch (IOException var7) {
            logger.error("", var7);
        } finally {
            IOUtils.closeQuietly(bais);
            IOUtils.closeQuietly(baos);
        }

        return null;
    }

    public static byte[] decompress(byte[] data) {
        ByteArrayInputStream bais = new ByteArrayInputStream(data);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();

        try {
            decompress(bais, baos);
            return baos.toByteArray();
        } catch (IOException var7) {
            logger.error("", var7);
        } finally {
            IOUtils.closeQuietly(bais);
            IOUtils.closeQuietly(baos);
        }
        return new byte[0];
    }

    public static void compress(InputStream is, OutputStream os) throws IOException {
        GZIPOutputStream gos = null;

        try {
            gos = new GZIPOutputStream(os);
            byte[] data = new byte[1024];

            int count;
            while ((count = is.read(data, 0, BUFFER)) != -1) {
                gos.write(data, 0, count);
            }
        } finally {
            IOUtils.closeQuietly(gos);
        }

    }

    public static void decompress(InputStream is, OutputStream os) throws IOException {
        GZIPInputStream gis = null;

        try {
            gis = new GZIPInputStream(is);
            byte[] data = new byte[BUFFER];
            int count;
            while ((count = gis.read(data, 0, BUFFER)) != -1) {
                os.write(data, 0, count);
            }
        } finally {
            IOUtils.closeQuietly(gis);
        }
    }
}
