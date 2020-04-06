package org.xuenan.itook.core.utils;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

public class HMacDigest {
    public HMacDigest() {
    }

    public static String hmacDigest(String msg, String keyString, String algo) {
        String digest = null;

        try {
            SecretKeySpec key = new SecretKeySpec(keyString.getBytes("UTF-8"), algo);
            Mac mac = Mac.getInstance(algo);
            mac.init(key);
            byte[] bytes = mac.doFinal(msg.getBytes("ASCII"));
            StringBuffer hash = new StringBuffer();

            for(int i = 0; i < bytes.length; ++i) {
                String hex = Integer.toHexString(255 & bytes[i]);
                if (hex.length() == 1) {
                    hash.append('0');
                }

                hash.append(hex);
            }

            digest = hash.toString();
        } catch (UnsupportedEncodingException var10) {
        } catch (InvalidKeyException var11) {
        } catch (NoSuchAlgorithmException var12) {
        }

        return digest;
    }
}
