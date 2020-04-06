package org.xuenan.itook.core.monitor;

public interface ShutdownMonitoring {
    void addTask(String taskId);

    void delTask(String taskId);

    boolean isShutdown();
}
