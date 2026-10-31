package com.seek.util.rabbitmqutil;

import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.connection.CorrelationData;

import java.util.UUID;

@Slf4j
public class CorrleationUtil {

    public CorrelationData createCorrelationData(String exchangeName, String routingKey, String correlationId) {
        //生成一个带有随机id的correlationData
        CorrelationData correlationData = new CorrelationData(correlationId);
        //设置回调函数
        correlationData.getFuture().whenComplete((r,e)->{
            if (e!=null)log.error("Exchange:{}发送消息到RoutingKey:{},发生异常",exchangeName,routingKey,e);
            if (!r.isAck())log.error("Exchange:{}发送消息到RoutingKey:{},未能成功到达交换机",exchangeName,routingKey);
        });
        return correlationData;
    }

    //产生CorrelationData
    public CorrelationData getCorrelation(String exchangeName, String routingKey){
        //生成一个带有随机id的correlationData
        return createCorrelationData(exchangeName,routingKey, UUID.randomUUID().toString());
    }

    //产生CorrelationData
    public CorrelationData getCorrelationWithId(String exchangeName,String routingKey,String correlationId){
        //生成一个带有指定id的correlationData
        return createCorrelationData(exchangeName,routingKey,correlationId);
    }
}
