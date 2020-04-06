package org.xuenan.itook.core.consts;

public enum  State {
    delete(-1),
    disable(0),
    enable(1);

    private final byte byteCode;
    private final short shortCode;
    private final int intCode;

    private State(final int code) {
        this.byteCode = (byte)code;
        this.shortCode = (short)code;
        this.intCode = code;
    }

    public byte byteCode() {
        return this.byteCode;
    }

    public short shortCode() {
        return this.shortCode;
    }

    public int intCode() {
        return this.intCode;
    }

    public boolean equals(final Byte byteCode) {
        return byteCode == null ? false : byteCode.equals(this.byteCode);
    }

    public boolean equals(final Short shortCode) {
        return shortCode == null ? false : shortCode.equals(this.shortCode);
    }

    public boolean equals(final Integer intCode) {
        return intCode == null ? false : intCode.equals(this.intCode);
    }

    public String toString() {
        return this.name();
    }
}
