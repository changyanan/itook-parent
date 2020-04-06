package org.xuenan.itook.core.utils;

import com.google.zxing.*;
import com.google.zxing.client.j2se.BufferedImageLuminanceSource;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.common.HybridBinarizer;
import javassist.NotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.Base64Utils;
import org.xuenan.itook.core.exception.GlobalException;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.Hashtable;
import java.util.Map;

public class QRCodeUtils {
    private static final Logger log = LoggerFactory.getLogger(QRCodeUtils.class);
    private static final int DEFAULT_HEIGHT = 320;
    private static final int DEFAULT_WIDTH = 320;

    public QRCodeUtils() {
    }

    public static String encodeToBase64(String content) {
        return encodeToBase64(content, (Integer)null, (Integer)null);
    }

    public static String encodeToBase64(String content, Integer width, Integer height) {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        encode(stream, content, width, height, "jpeg");
        byte[] bytes = stream.toByteArray();
        return Base64Utils.encodeToString(bytes);
    }

    public static void encode(OutputStream stream, String content, Integer width, Integer height, String fileType) {
        Assert.hasText(content, "二维码内容不可以为空", new Object[0]);
        if (width == null || width == 0) {
            width = 320;
        }

        if (height == null || height == 0) {
            height = 320;
        }

        Map<EncodeHintType, String> hints = new Hashtable();
        hints.put(EncodeHintType.CHARACTER_SET, "UTF-8");

        try {
            BitMatrix bitMatrix = (new MultiFormatWriter()).encode(content, BarcodeFormat.QR_CODE, width, height, hints);
            MatrixToImageWriter.writeToStream(bitMatrix, fileType, stream);
        } catch (WriterException | IOException var7) {
            var7.printStackTrace();
        }

    }

    public static String decodeBase64(String base64) {
        byte[] bytes = Base64Utils.decodeFromString(base64);
        InputStream inputStream = new ByteArrayInputStream(bytes);
        return decode(inputStream);
    }

    public static String decode(InputStream inputStream) {
        try {
            BufferedImage image = ImageIO.read(inputStream);
            LuminanceSource source = new BufferedImageLuminanceSource(image);
            BinaryBitmap bitmap = new BinaryBitmap(new HybridBinarizer(source));
            Hashtable<DecodeHintType, String> hints = new Hashtable<>();
            hints.put(DecodeHintType.CHARACTER_SET, "UTF-8");
            return (new MultiFormatReader()).decode(bitmap, hints).getText();
        } catch (com.google.zxing.NotFoundException | IOException var5) {
            log.warn("解码二维码失败", var5);
            GlobalException.warn("解码二维码失败");
            return null;
        }
    }
}
