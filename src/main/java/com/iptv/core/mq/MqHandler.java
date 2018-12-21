package com.iptv.core.mq;

import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageListener;

public class MqHandler implements MessageListener {
    @Override
    public void onMessage(Message msg) {
        try{
            System.out.println(new String(msg.getBody()));
        }catch(Exception e){
            e.printStackTrace();
        }
    }
}
