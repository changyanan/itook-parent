package org.xuenan.itook.core.utils.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.xuenan.itook.core.utils.Zip;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

@Component
public class Jdkzip implements Zip {
    private static final Logger LOGGER = LoggerFactory.getLogger(Jdkzip.class);

    public Jdkzip() {
    }

    public byte[] z(byte[] data) {
        ByteArrayOutputStream bos = null;
        ZipOutputStream zip = null;

        Object var5;
        try {
            bos = new ByteArrayOutputStream();
            zip = new ZipOutputStream(bos);
            ZipEntry entry = new ZipEntry("zip");
            entry.setSize((long)data.length);
            zip.putNextEntry(entry);
            zip.write(data);
            zip.closeEntry();
            byte[] var21 = bos.toByteArray();
            return var21;
        } catch (Exception var19) {
            LOGGER.warn("", var19);
            var5 = null;
        } finally {
            if (zip != null) {
                try {
                    zip.close();
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
        ZipInputStream zip = null;
        ByteArrayOutputStream baos = null;

        try {
            bis = new ByteArrayInputStream(data);
            zip = new ZipInputStream(bis);
            baos = new ByteArrayOutputStream();
            if (zip.getNextEntry() == null) {
                Object var30 = null;
                return (byte[])var30;
            } else {
                byte[] buf = new byte[1024];
                boolean var31 = true;

                int num;
                while((num = zip.read(buf, 0, buf.length)) != -1) {
                    baos.write(buf, 0, num);
                }

                byte[] var7 = baos.toByteArray();
                return var7;
            }
        } catch (Exception var28) {
            LOGGER.warn("", var28);
            Object var6 = null;
            return (byte[])var6;
        } finally {
            if (zip != null) {
                try {
                    zip.close();
                } catch (IOException var27) {
                    LOGGER.warn("", var27);
                }
            }

            if (bis != null) {
                try {
                    bis.close();
                } catch (IOException var26) {
                    LOGGER.warn("", var26);
                }
            }

            if (baos != null) {
                try {
                    baos.close();
                } catch (IOException var25) {
                    LOGGER.warn("", var25);
                }
            }

        }
    }
}
