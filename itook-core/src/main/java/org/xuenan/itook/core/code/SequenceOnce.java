package org.xuenan.itook.core.code;

public class SequenceOnce {
    String timestampString;
    String serverIdString;
    String sequenceString;

    public SequenceOnce() {
    }

    public String toString() {
        StringBuffer buffer = new StringBuffer();
        buffer.append(this.timestampString);
        buffer.append('-');
        buffer.append(this.serverIdString);
        buffer.append('-');
        buffer.append(this.sequenceString);
        return buffer.toString();
    }
}
