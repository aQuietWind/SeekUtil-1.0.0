package com.seek.util.rabbitmqutil;

import com.seek.util.configobject.RabbitMQData.QueueData;
import org.springframework.amqp.core.Queue;
import org.springframework.context.annotation.Lazy;

import java.util.HashMap;
import java.util.Map;

@Lazy
public class QueueUtil {

    //生成一个仲裁队列
    public Queue generateQuorumQueue(QueueData queue){
        Map<String, Object> args = new HashMap<>();
        //设置队列模式为quorum仲裁模式
        args.put("x-queue-type", "quorum");
        return new Queue(queue.getQueueName(), true, false, false, args);
    }

    //生成一个死信仲裁队列
    public Queue getDeadQuorumQueue(QueueData queue,QueueData deadQueue){
        Map<String, Object> args = new HashMap<>();
        //设置队列模式为quorum仲裁模式
        args.put("x-queue-type", "quorum");
        //绑定死信交换机
        args.put("x-dead-letter-exchange", deadQueue.getExchangeName());
        //死信转发时使用的routingKey
        args.put("x-dead-letter-routing-key", deadQueue.getRoutingKey());
        return new Queue(queue.getQueueName(), true, false, false, args);
    }

}
