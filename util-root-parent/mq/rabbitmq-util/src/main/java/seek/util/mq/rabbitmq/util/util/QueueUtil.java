package seek.util.mq.rabbitmq.util.util;

import org.springframework.amqp.core.Queue;
import seek.util.common.config.object.mq.rabbitmq.BindingData;

import java.util.HashMap;
import java.util.Map;

public class QueueUtil {

    //生成一个仲裁队列
    public Queue generateQuorumQueue(String queueName){
        Map<String, Object> args = new HashMap<>();
        //设置队列模式为quorum仲裁模式
        args.put("x-queue-type", "quorum");
        return new Queue(queueName, true, false, false, args);
    }

    //生成一个死信仲裁队列
    public Queue getDeadQuorumQueue(BindingData bindingData){
        Map<String, Object> args = new HashMap<>();
        //设置队列模式为quorum仲裁模式
        args.put("x-queue-type", "quorum");
        //绑定死信交换机
        args.put("x-dead-letter-exchange", bindingData.getExchange().getExchangeName());
        //死信转发时使用的routingKey
        args.put("x-dead-letter-routing-key", bindingData.getRoutingKey());
        return new Queue(bindingData.getQueueName(), true, false, false, args);
    }

}
