package seek.util.mq.rabbitmq.util.configuration;

import seek.util.mq.rabbitmq.util.util.QueueUtil;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.FanoutExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.rabbit.retry.MessageRecoverer;
import org.springframework.amqp.rabbit.retry.RepublishMessageRecoverer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ErrorExchangeConfig {
    private final QueueUtil queueUtil;
    private final RabbitTemplate rabbitTemplate;
    private final PropertiesConfig propertiesConfig;

    @Autowired
    public ErrorExchangeConfig(QueueUtil queueUtil, RabbitTemplate rabbitTemplate, PropertiesConfig propertiesConfig) {
        this.queueUtil = queueUtil;
        this.rabbitTemplate = rabbitTemplate;
        this.propertiesConfig = propertiesConfig;
    }

    //创建一个用于失败消息接受的队列
    @Bean
    public Queue errorQueue(){
        return queueUtil.generateQuorumQueue(propertiesConfig.getErrorBinding().getQueueName());
    }
    //创建一个用于失败消息投递的交换机
    @Bean
    public FanoutExchange errorExchange(){
        return new FanoutExchange(propertiesConfig.getErrorExchange().getExchangeName());
    }
    //绑定交换机与队列
    @Bean
    public Binding errorBinding(Queue errorQueue, FanoutExchange errorExchange){
        return BindingBuilder.bind(errorQueue).to(errorExchange);
    }
    @Bean
    public MessageRecoverer recoverer(){
        //声明目标交换机以及key，返回该处理器
        return new RepublishMessageRecoverer(rabbitTemplate, propertiesConfig.getErrorExchange().getExchangeName());
    }
}