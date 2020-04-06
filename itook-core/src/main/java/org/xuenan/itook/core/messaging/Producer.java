package org.xuenan.itook.core.messaging;

import com.google.common.eventbus.AsyncEventBus;
import com.google.common.eventbus.EventBus;

import java.util.concurrent.Executors;

public class Producer {
    private static final EventBus eventBus = new EventBus();
    private static final AsyncEventBus asyncEventBus = new AsyncEventBus(Executors.newFixedThreadPool(16));

    public Producer() {
    }

    public static final void asyncPublish(Message event) {
        asyncEventBus.post(event);
    }

    public static final void publish(Message event) {
        eventBus.post(event);
    }

    protected static final <Observer> void register(Observer observer) {
        asyncEventBus.register(observer);
        eventBus.register(observer);
    }
}
