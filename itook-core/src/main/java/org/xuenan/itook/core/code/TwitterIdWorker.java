package org.xuenan.itook.core.code;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TwitterIdWorker implements IdWorker{
    private static final Logger log = LoggerFactory.getLogger(TwitterIdWorker.class);
    private static final long TWEPOCH = 1288711299999L;
    private static final int WORKER_ID_BITS = 7;
    private static final int MAX_WORKER_ID = 127;
    private static final int SEQUENCE_BITS = 15;
    private static final int WORKER_ID_SHIFT = 15;
    private static final int TIMESTAMP_LEFT_SHIFT = 22;
    private static final int SEQUENCE_MASK = 32767;
    private int workerId = -1;
    private volatile long sequence = 0L;
    private volatile long lastTimestamp = -1L;

    public TwitterIdWorker() {
    }

    public TwitterIdWorker(int workerId) {
        this.workerId = workerId;
    }

    public void setWorkerId(int workerId) {
        if (workerId <= 127 && workerId >= 0) {
            this.workerId = workerId;
        } else {
            throw new IllegalArgumentException(String.format("worker Id can't be greater than %d or less than 0", 127));
        }
    }

    public synchronized long nextId() {
        long timestamp = this.timeGen();
        if (this.lastTimestamp == timestamp) {
            this.sequence = this.sequence + 1L & 32767L;
            if (this.sequence == 0L) {
                if (log.isDebugEnabled()) {
                    log.debug("########### sequenceMask={}", 32767);
                }

                timestamp = this.tilNextMillis(this.lastTimestamp);
            }
        } else {
            this.sequence = 0L;
        }

        if (timestamp < this.lastTimestamp) {
            throw new IllegalArgumentException(String.format("Clock moved backwards.  Refusing to generate id for %d milliseconds", this.lastTimestamp - timestamp));
        } else {
            this.lastTimestamp = timestamp;
            long nextId = timestamp - 1288711299999L << 22 | (long)(this.workerId << 15) | this.sequence;
            if (log.isTraceEnabled()) {
                log.trace("timestamp:{},timestampLeftShift:{},nextId:{},workerId:{},sequence:{}", new Object[]{timestamp, 22, nextId, this.workerId, this.sequence});
            }

            return nextId;
        }
    }

    private long tilNextMillis(final long lastTimestamp) {
        long timestamp;
        for(timestamp = this.timeGen(); timestamp <= lastTimestamp; timestamp = this.timeGen()) {
        }

        return timestamp;
    }

    private long timeGen() {
        return System.currentTimeMillis();
    }
}
