package seek.util.common.config.object.RabbitMQData;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class QueueData {
    private String exchangeName;
    private String queueName;
    private String routingKey;
}
