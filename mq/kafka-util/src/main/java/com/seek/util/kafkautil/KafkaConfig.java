package com.seek.util.kafkautil;

import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;
import org.springframework.kafka.support.ProducerListener;

@Slf4j
@Configuration
public class KafkaConfig {

    @Bean
    public ProducerListener<String, Object> kafkaProducerListener() {
        return new ProducerListener<String,Object>() {
            @Override
            public void onSuccess(ProducerRecord<String, Object> producerRecord, RecordMetadata recordMetadata) {
                // 所有消息发送成功统一入口
            }

            @Override
            public void onError(ProducerRecord<String, Object> producerRecord, RecordMetadata recordMetadata, Exception exception) {
                // 所有消息发送失败统一入口
                log.error("kafka发送消息至Topic:{}时失败,异常:", producerRecord.topic(),exception);
            }
        };
    }

    // 将listener绑定到kafkaTemplate
    @Bean
    public KafkaTemplate<String, Object> kafkaTemplate(ProducerFactory<String, Object> factory, ProducerListener<String, Object> listener) {
        KafkaTemplate<String, Object> template = new KafkaTemplate<>(factory);
        template.setProducerListener(listener);
        return template;
    }





}