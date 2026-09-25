package com.seek.util.rabbitmqutil.Config;

import com.seek.util.configobject.RabbitMQData.QueueData;
import com.seek.util.rabbitmqutil.QueueUtil;
import jakarta.annotation.PostConstruct;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.FanoutExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.rabbit.retry.MessageRecoverer;
import org.springframework.amqp.rabbit.retry.RepublishMessageRecoverer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ErrorExchangeConfig {
    @Value("${seek.util.rabbitmq.config.error-exchange.exchange-name}")
    private String exchangeName;
    @Value("${seek.util.rabbitmq.config.error-exchange.queue-name}")
    private String queueName;
    private QueueData queue;
    private final QueueUtil queueUtil;
    private final RabbitTemplate rabbitTemplate;

    @Autowired
    public ErrorExchangeConfig(QueueUtil queueUtil, RabbitTemplate rabbitTemplate) {
        this.queueUtil = queueUtil;
        this.rabbitTemplate = rabbitTemplate;
    }

    @PostConstruct
    public void init() {
        //提供默认的配置
        queue = new QueueData(exchangeName,queueName,null);
    }

    //创建一个用于失败消息接受的队列
    @Bean
    public Queue errorQueue(){
        return queueUtil.generateQuorumQueue(queue);
    }
    //创建一个用于失败消息投递的交换机
    @Bean
    public FanoutExchange errorExchange(){
        return new FanoutExchange(queue.getExchangeName());
    }
    //绑定交换机与队列
    @Bean
    public Binding errorBinding(Queue errorQueue, FanoutExchange errorExchange){
        return BindingBuilder.bind(errorQueue).to(errorExchange);
    }
    @Bean
    public MessageRecoverer recoverer(){
        //声明目标交换机以及key，返回该处理器
        return new RepublishMessageRecoverer(rabbitTemplate,queue.getExchangeName());
    }
}