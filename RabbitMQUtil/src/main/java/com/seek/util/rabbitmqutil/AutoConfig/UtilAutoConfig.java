package com.seek.util.rabbitmqutil.AutoConfig;

import com.seek.util.rabbitmqutil.CorrleationUtil;
import com.seek.util.rabbitmqutil.MQUtil;
import com.seek.util.rabbitmqutil.QueueUtil;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;

@Configuration
public class UtilAutoConfig {
    @Bean
    @Lazy
    public CorrleationUtil corrleationUtil() {
        return new CorrleationUtil();
    }
    @Bean
    @Lazy
    public MQUtil mqUtil(RabbitTemplate rabbitTemplate,CorrleationUtil corrleationUtil) {
        return new MQUtil(rabbitTemplate,corrleationUtil);
    }
    @Bean
    @Lazy
    public QueueUtil queueUtil() {
        return new QueueUtil();
    }
}
