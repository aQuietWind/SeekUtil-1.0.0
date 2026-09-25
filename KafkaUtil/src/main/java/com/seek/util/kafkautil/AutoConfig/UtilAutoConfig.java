package com.seek.util.kafkautil.AutoConfig;

import com.seek.util.kafkautil.KafkaUtil;
import com.seek.util.kafkautil.TopicUtil;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.kafka.core.KafkaTemplate;

@Configuration
public class UtilAutoConfig {
    @Bean
    @Lazy
    public KafkaUtil kafkaUtil(KafkaTemplate<String, Object> kafkaTemplate) {
        return new KafkaUtil(kafkaTemplate);
    }
    @Bean
    @Lazy
    public TopicUtil topicUtil() {
        return new TopicUtil();
    }
}
