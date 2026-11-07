package seek.util.common.config.object.mq.rabbitmq;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
//生产者发送消息用
public class DestinationData {
    private String exchangeName;
    private String routingKey;
}
