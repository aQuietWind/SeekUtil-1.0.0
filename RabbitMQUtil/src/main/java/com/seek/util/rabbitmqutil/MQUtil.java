package com.seek.util.rabbitmqutil;

import com.seek.util.configobject.RabbitMQData.QueueData;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.UUID;

@Slf4j
public class MQUtil {

    private final RabbitTemplate rabbitTemplate;
    private final CorrleationUtil corrleationUtil;

    @Autowired
    public MQUtil(RabbitTemplate rabbitTemplate, CorrleationUtil corrleationUtil) {
        this.rabbitTemplate = rabbitTemplate;
        this.corrleationUtil = corrleationUtil;
    }



    //发送消息
    public void send(QueueData queue, Object message){
        rabbitTemplate.convertAndSend(queue.getExchangeName(),queue.getRoutingKey(),message,corrleationUtil.getCorrelation(queue.getExchangeName(), queue.getRoutingKey()));
    }


    //发送带过期时间的消息并且返回letterId使用
    public String sendWithTLLAndGetId(QueueData queue, Object message,String tllMilliSeconds){
        String letterId=UUID.randomUUID().toString();
        rabbitTemplate.convertAndSend(queue.getExchangeName(),queue.getRoutingKey()
                ,message
                , msg->{
            //设置ttl
            msg.getMessageProperties().setExpiration(tllMilliSeconds);
            msg.getMessageProperties().setMessageId(letterId);
            return msg;}
                ,corrleationUtil.getCorrelationWithId(queue.getExchangeName(), queue.getRoutingKey(),letterId));
        return letterId;
    }

    //发送带过期时间的消息
    public void sendWithTLL(QueueData queue, Object message ,String tllMilliSeconds){
        rabbitTemplate.convertAndSend(queue.getExchangeName(),queue.getRoutingKey()
                ,message
                , msg->{
            //设置ttl
            msg.getMessageProperties().setExpiration(tllMilliSeconds);
            return msg;}
                ,corrleationUtil.getCorrelation(queue.getExchangeName(), queue.getRoutingKey()));
    }

    //分钟转毫秒
    public String minuteToMillis(int minute){
        return ""+minute*60*1000;
    }

}
