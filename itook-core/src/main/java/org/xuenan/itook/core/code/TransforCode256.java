package org.xuenan.itook.core.code;

public class TransforCode256 {
    static final TransforCode CODE_STRING = new TransforCode((short)256);

    public TransforCode256() {
    }

    public static final Long deserialize(byte[] codes) {
        return codes == null ? null : CODE_STRING.deserialize(codes);
    }

    public static final byte[] serialize(Long _long) {
        return _long == null ? null : CODE_STRING.serialize(_long);
    }

    public static final short getLength() {
        return (short)CODE_STRING.getLength();
    }
}
