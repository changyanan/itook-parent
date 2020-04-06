package org.xuenan.itook.core.utils;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.core.task.TaskExecutor;
import org.springframework.stereotype.Component;
import org.xuenan.itook.core.task.TaskProperties;

import java.util.concurrent.atomic.AtomicInteger;

@Component
@ComponentScan
@EnableConfigurationProperties({TaskProperties.class})
public class AsyncUtils {
    static final AtomicInteger id = new AtomicInteger();
    static TaskExecutor executor;

    public AsyncUtils(TaskProperties taskProperties) {
        executor = taskProperties.getTask();
    }

    public static <T> void execute(AsyncUtils.AsyncEvent<T> event, T t) {
        executor.execute(() -> event.apply(t));
    }

    public static <T> void execute(AsyncUtils.AsyncEventPart<T> event) {
        executor.execute(event::apply);
    }

    public interface AsyncEventPart<T> {
        void apply();
    }

    public interface AsyncEvent<T> {
        void apply(T t);
    }
}
