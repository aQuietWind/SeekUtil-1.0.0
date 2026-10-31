package seek.util.common.config.object.mq.rocketmq;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ConsumerData {
    private String consumerGroupName;
    private String tag;
}
