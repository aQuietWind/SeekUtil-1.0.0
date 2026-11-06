package seek.util.mq.rabbitmq.util.config;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.boot.context.properties.ConfigurationProperties;
import seek.util.common.config.object.mq.rabbitmq.QueueData;

@ConfigurationProperties("seek.util.rabbitmq.config")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PropertiesConfig {
    private QueueData errorExchange;
    private String converterPackage;
}
