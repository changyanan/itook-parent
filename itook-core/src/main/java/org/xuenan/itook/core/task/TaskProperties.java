package org.xuenan.itook.core.task;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.ThreadPoolExecutor;
@ConfigurationProperties("globalegrow")
public class TaskProperties {
    private static final long serialVersionUID = 1L;
    static final String prefix = "itook";
    private ThreadPoolTaskExecutor task = new ThreadPoolTaskExecutor();

    public TaskProperties() {
        this.task.setThreadNamePrefix("SCG-TASK-");
        this.task.setMaxPoolSize(1000);
        this.task.setCorePoolSize(1000);
        this.task.setKeepAliveSeconds(3600);
        this.task.setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy());
    }

    public ThreadPoolTaskExecutor getTask() {
        return this.task;
    }

    public void setTask(ThreadPoolTaskExecutor task) {
        this.task = task;
    }
}
