package com.seek.util.rabbitmqutil.Config;

import org.springframework.amqp.support.converter.DefaultJackson2JavaTypeMapper;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ConverterConfig {
    //序列化工具，用于对复杂的Object传递
    @Value("${seek.util.rabbitmq.config.package}")
    private String packageName;

    @Bean
    public MessageConverter messageConverter() {
        Jackson2JsonMessageConverter converter = new Jackson2JsonMessageConverter();
        DefaultJackson2JavaTypeMapper typeMapper = new DefaultJackson2JavaTypeMapper();
        //添加根包
        typeMapper.setTrustedPackages(packageName);
        converter.setJavaTypeMapper(typeMapper);
        return converter;
    }
}
