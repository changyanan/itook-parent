package org.xuenan.itook.core.code;

public class TransforCode36String {
    static final TransforCode CODE_STRING = new TransforCode("0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZ".getBytes());

    public TransforCode36String() {
    }

    public static TransforCode getCodeString() {
        return CODE_STRING;
    }

    public static final Long deserialize(String codes) {
        return codes == null ? null : CODE_STRING.deserialize(codes.getBytes());
    }

    public static final String serialize(Long _long) {
        return _long == null ? null : new String(CODE_STRING.serialize(_long));
    }

    public static final byte getLength() {
        return (byte)CODE_STRING.getLength();
    }
}
