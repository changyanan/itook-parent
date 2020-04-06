package org.xuenan.itook.core.code;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.atomic.AtomicInteger;

public final class Sequence36 {
    private static final Logger LOGGER = LoggerFactory.getLogger(Sequence36.class);
    private static final TransforCode CODE_STRING;
    private static final short LENGTH;
    private static final byte EMPTY;
    private static final int TIMESTAMP_SHIFT = 10;
    private static final int SEQUENCE_SHIFT = 4;
    private static final int SERVERID_SHIFT = 2;
    private static final int SEQUENCE_MAX;
    private static final int SERVERID_MAX;
    private static String lastTimestampString;
    private static volatile long lastTimestamp;
    private static AtomicInteger serverid;
    private static String serveridStr;
    private static AtomicInteger atomicSequence;

    public Sequence36() {
    }

    public static final SequenceOnce generate() {
        validServerid();
        SequenceOnce sequenceOnce = new SequenceOnce();
        sequenceOnce.serverIdString = serveridStr;
        nextSequence(sequenceOnce);
        return sequenceOnce;
    }

    private static final void nextSequence(SequenceOnce sequenceOnce) {
        int sequence = atomicSequence.updateAndGet((operand) -> {
            long runTime = System.currentTimeMillis();
            if (runTime == lastTimestamp && operand < SEQUENCE_MAX) {
                sequenceOnce.timestampString = lastTimestampString;
                return operand + 1;
            } else if (runTime < lastTimestamp) {
                if (runTime < lastTimestamp) {
                    throw new RuntimeException("时间不可以向前调整");
                } else {
                    return 0;
                }
            } else {
                if (operand >= SEQUENCE_MAX) {
                    while(true) {
                        if (runTime != System.currentTimeMillis()) {
                            runTime = System.currentTimeMillis();
                            break;
                        }
                    }
                }

                byte[] tc = CODE_STRING.serialize(runTime);
                lastTimestampString = addFormat(tc, 10);
                sequenceOnce.timestampString = lastTimestampString;
                lastTimestamp = runTime;
                return 0;
            }
        });
        byte[] tc = CODE_STRING.serialize((long)sequence);
        sequenceOnce.sequenceString = addFormat(tc, 4);
    }

    private static void validServerid() {
        if (Sequence36.serverid.compareAndSet(0, 1)) {
            short serverid = (short)((int)(Math.random() * (double)SERVERID_MAX));
            setServerid(serverid);
            LOGGER.warn("请注意,Sequence序列未设置服务器id,多台服务器有可能会生成相同的序列,随机产生的服务器id {}", serverid);
        }

    }

    private static String addFormat(byte[] tc, int length) {
        byte[] buffer = new byte[length];

        for(int i = 0; i < length - tc.length; ++i) {
            buffer[i] = EMPTY;
        }

        System.arraycopy(tc, 0, buffer, length - tc.length, tc.length);
        return new String(buffer);
    }

    public static short getServerid() {
        long serverid = CODE_STRING.deserialize(serveridStr.getBytes());
        return (short)((int)serverid);
    }

    public static synchronized void setServerid(int i) {
        if (i >= 0 && i <= SERVERID_MAX) {
            serverid.set(i);
            byte[] st = CODE_STRING.serialize((long)i);
            serveridStr = addFormat(st, 2);
        } else {
            throw new RuntimeException("serverid必须大于0且小于" + SERVERID_MAX);
        }
    }

    static {
        CODE_STRING = TransforCode36String.CODE_STRING;
        LENGTH = (short)CODE_STRING.getLength();
        EMPTY = CODE_STRING.serialize(0L)[0];
        SEQUENCE_MAX = (int)Math.pow((double)LENGTH, 4.0D) - 1;
        SERVERID_MAX = (int)Math.pow((double)LENGTH, 2.0D) - 1;
        lastTimestampString = "";
        lastTimestamp = -1L;
        serverid = new AtomicInteger(0);
        serveridStr = new String(new byte[]{EMPTY, EMPTY});
        atomicSequence = new AtomicInteger(0);
    }
}
