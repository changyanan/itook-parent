package org.xuenan.itook.core.task;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

@Configuration
@EnableConfigurationProperties({TaskProperties.class})
public class TaskAutoConfiguration {
    private static final Logger log = LoggerFactory.getLogger(TaskAutoConfiguration.class);
    @Autowired
    TaskProperties properties;

    public TaskAutoConfiguration() {
    }

    @Bean
    public ThreadPoolTaskExecutor taskExecutor() {
        ThreadPoolTaskExecutor taskExecutor = this.properties.getTask();
        log.info("创建的线程池PoolSize {} ,MaxPoolSize {}", taskExecutor.getPoolSize(), taskExecutor.getMaxPoolSize());
        return taskExecutor;
    }
}
