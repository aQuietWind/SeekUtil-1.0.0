package seek.util.mq.kafka.util.autoconfigure;

import seek.util.mq.kafka.util.config.KafkaConfig;
import seek.util.mq.kafka.util.util.KafkaUtil;
import seek.util.mq.kafka.util.util.TopicUtil;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.kafka.core.KafkaTemplate;

@Configuration
public class UtilAutoConfig {
    @Bean
    public KafkaUtil kafkaUtil(KafkaTemplate<String, Object> kafkaTemplate) {
        return new KafkaUtil(kafkaTemplate);
    }
    @Bean
    public TopicUtil topicUtil() {
        return new TopicUtil();
    }

    @Bean
    public KafkaConfig kafkaConfig() {
        return new KafkaConfig();
    }
}
