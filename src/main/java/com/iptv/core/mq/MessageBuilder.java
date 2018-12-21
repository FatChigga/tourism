package com.iptv.core.mq;

import com.iptv.core.utils.BaseUtil;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.stereotype.Service;

@Service
public class MessageBuilder {
    public void sendDataToQueue(String queueKey, Object object){
        AmqpTemplate amqpTemplate = (AmqpTemplate) BaseUtil.getService("amqpTemplate");
        amqpTemplate.convertAndSend(queueKey,object);
    }
}
