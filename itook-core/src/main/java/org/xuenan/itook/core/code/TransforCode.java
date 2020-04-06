package org.xuenan.itook.core.code;

import org.xuenan.itook.core.utils.Assert;

public class TransforCode {
    private static final int BYTE_SIZE = 64;
    private final byte[] bytes;
    private final int length;

    public TransforCode(final byte[] bytes) {
        this.length = bytes.length;
        this.bytes = bytes;
    }

    public TransforCode(final short length) {
        Assert.isTrue(length > 0, "序列化字符串不可以小于0");
        Assert.isTrue(length <= 256, "序列化字符串不可以大于256");
        byte[] bytes = new byte[length];
        short len = (short)(length / 2);

        for(short i = 0; i < length; ++i) {
            bytes[i] = (byte)(i - len);
        }

        this.length = bytes.length;
        this.bytes = bytes;
    }

    public byte[] serialize(Long len) {
        if (len == null) {
            return null;
        } else {
            byte[] data = new byte[64];

            int i;
            for(i = 1; i <= 64; ++i) {
                data[64 - i] = this.bytes[(int)(len % (long)this.length)];
                if (len / (long)this.length == 0L) {
                    break;
                }
            }

            byte[] ret = new byte[i];
            System.arraycopy(data, 64 - i, ret, 0, i);
            return ret;
        }
    }

    public Long deserialize(final byte[] _bytes) {
        if (_bytes == null) {
            return null;
        } else {
            long lval = 0L;
            byte[] var4 = _bytes;
            int var5 = _bytes.length;

            for(int var6 = 0; var6 < var5; ++var6) {
                byte _byte = var4[var6];

                for(int i = 0; i < this.bytes.length; ++i) {
                    if (this.bytes[i] == _byte) {
                        lval = lval * (long)this.length + (long)i;
                        break;
                    }
                }
            }

            return lval;
        }
    }

    public int getLength() {
        return this.length;
    }
}
