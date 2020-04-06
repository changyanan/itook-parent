package org.xuenan.itook.core.utils.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;
import org.xuenan.itook.core.utils.Zip;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

@Primary
@Component
public class Gzip implements Zip {
    private static final Logger LOGGER = LoggerFactory.getLogger(Gzip.class);

    public Gzip() {
    }

    public byte[] z(byte[] data) {
        ByteArrayOutputStream bos = null;
        GZIPOutputStream gzip = null;

        Object var5;
        try {
            bos = new ByteArrayOutputStream();
            gzip = new GZIPOutputStream(bos);
            gzip.write(data);
            gzip.finish();
            byte[] var4 = bos.toByteArray();
            return var4;
        } catch (Exception var19) {
            LOGGER.warn("", var19);
            var5 = null;
        } finally {
            if (gzip != null) {
                try {
                    gzip.close();
                } catch (IOException var18) {
                    LOGGER.warn("", var18);
                }
            }

            if (bos != null) {
                try {
                    bos.close();
                } catch (IOException var17) {
                    LOGGER.warn("", var17);
                }
            }

        }

        return (byte[])var5;
    }

    public byte[] un(byte[] data) {
        ByteArrayInputStream bis = null;
        GZIPInputStream gzip = null;
        ByteArrayOutputStream baos = null;

        Object var6;
        try {
            bis = new ByteArrayInputStream(data);
            gzip = new GZIPInputStream(bis);
            byte[] buf = new byte[1024];
//            int num = true;
            baos = new ByteArrayOutputStream();

            int num;
            while((num = gzip.read(buf, 0, buf.length)) != -1) {
                baos.write(buf, 0, num);
            }

            byte[] var7 = baos.toByteArray();
            return var7;
        } catch (Exception var25) {
            LOGGER.warn("", var25);
            var6 = null;
        } finally {
            if (gzip != null) {
                try {
                    gzip.close();
                } catch (IOException var24) {
                    LOGGER.warn("", var24);
                }
            }

            if (baos != null) {
                try {
                    baos.close();
                } catch (IOException var23) {
                    LOGGER.warn("", var23);
                }
            }

            if (bis != null) {
                try {
                    bis.close();
                } catch (IOException var22) {
                    LOGGER.warn("", var22);
                }
            }

        }

        return (byte[])var6;
    }
}
