package org.xuenan.itook.core.monitor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextClosedEvent;

import java.util.Set;
import java.util.concurrent.ConcurrentSkipListSet;
import java.util.concurrent.atomic.AtomicBoolean;

public class ShutdownMonitoringImpl implements ShutdownMonitoring, ApplicationListener<ContextClosedEvent> {
    private static final Logger log = LoggerFactory.getLogger(ShutdownMonitoringImpl.class);
    private static final Set<String> TASK_IDS = new ConcurrentSkipListSet();
    private static final AtomicBoolean STOP_TASK = new AtomicBoolean(false);
    private static final AtomicBoolean STOP_OVER = new AtomicBoolean(false);

    public ShutdownMonitoringImpl() {
    }

    public void onApplicationEvent(ContextClosedEvent event) {
        STOP_TASK.set(true);

        try {
            int num = 0;

            while(!TASK_IDS.isEmpty()) {
                if (num > 3000) {
                    log.error("超过{}秒任务依然未执行完毕 ,程序强制关机，任务ids{} ", (double)num / 10.0D, TASK_IDS);
                    break;
                }

                if (num > 600) {
                    log.warn("超过一分钟，仍然有{}任务未执行完毕", TASK_IDS);
                }

                ++num;
                Thread.sleep(100L);
            }
        } catch (InterruptedException var6) {
            log.warn("关机异常", var6);
        } finally {
            STOP_OVER.set(true);
        }

    }

    public boolean isShutdown() {
        return STOP_TASK.get();
    }

    public void addTask(String taskId) {
        TASK_IDS.add(taskId);
    }

    public void delTask(String taskId) {
        TASK_IDS.remove(taskId);
    }
}
