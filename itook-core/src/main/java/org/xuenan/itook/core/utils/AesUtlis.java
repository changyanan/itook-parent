package org.xuenan.itook.core.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.xuenan.itook.core.exception.GlobalException;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import java.security.Key;

public abstract class AesUtlis {
    private static Logger logger = LoggerFactory.getLogger(AesUtlis.class);
    private static final String ALGORITHMSTR = "AES/ECB/PKCS5Padding";

    public AesUtlis() {
    }

    public static byte[] encrypt(String content, String secretKey) {
        try {
            Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
            cipher.init(1, getAesKey(secretKey));
            return cipher.doFinal(content.getBytes("utf-8"));
        } catch (Exception var3) {
            logger.warn("加密内容失败 :{}", var3.getMessage());
            GlobalException.warn("内容加密失败", new Object[0]);
            return null;
        }
    }

    public static final String decrypt(byte[] encryptBytes, String secretKey) {
        try {
            Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
            cipher.init(2, getAesKey(secretKey));
            byte[] decryptBytes = cipher.doFinal(encryptBytes);
            return new String(decryptBytes);
        } catch (Exception var4) {
            logger.warn("解密内容失败:{}", var4.getMessage());
            GlobalException.warn("内容解密失败", new Object[0]);
            return null;
        }
    }

    private static final Key getAesKey(String secretKey) {
        return new SecretKeySpec(secretKey.getBytes(), "AES");
    }

}
