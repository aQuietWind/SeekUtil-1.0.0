package seek.util.common.config.object.mq.rabbitmq;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
//绑定队列与交换机关系用
public class BindingData {
    private ExchangeData exchange;
    private String queueName;
    private String routingKey;
}
