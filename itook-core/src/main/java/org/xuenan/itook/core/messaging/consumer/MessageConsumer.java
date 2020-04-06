package org.xuenan.itook.core.messaging.consumer;

import com.google.common.eventbus.Subscribe;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.xuenan.itook.core.messaging.Message;

public class MessageConsumer {
    private static final Logger logger = LoggerFactory.getLogger(MessageConsumer.class);

    public MessageConsumer() {
    }

    @Subscribe
    public void message(Message message) {
        logger.debug("收到消息 : {}", message);
    }
}
