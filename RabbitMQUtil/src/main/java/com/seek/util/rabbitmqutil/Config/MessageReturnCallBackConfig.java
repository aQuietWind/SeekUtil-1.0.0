package com.seek.util.rabbitmqutil.Config;


import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.Configuration;


//该类用于交换机无法将信息发送给队列的情况
@Configuration
@Slf4j
public class MessageReturnCallBackConfig implements ApplicationContextAware {

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        //获取之前注入的rabbitTemplate
        RabbitTemplate rabbitTemplate = applicationContext.getBean(RabbitTemplate.class);
        //当交换机无法将消息发送给队列时触发
        rabbitTemplate.setReturnsCallback(all->{
            log.error("消息{}从交换机{}通过路由key:{}发送给队列{}失败,code:{}",
                    all.getMessage(),
                    all.getExchange(),
                    all.getRoutingKey(),
                    all.getReplyText(),
                    all.getReplyCode());
        });
    }
}

