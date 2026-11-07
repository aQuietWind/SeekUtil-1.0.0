package seek.util.common.event.respond.system.config;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties("seek.util.common.event-respond-system.config")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class PropertiesConfig {
    private PoolConfig checkPool = new PoolConfig(2,4,60);
    private PoolConfig delayPool = new PoolConfig(4,8,60);
    private PoolConfig executePool = new PoolConfig(4,8,60);

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class PoolConfig{
        private Integer threadCoreSize;
        private Integer threadMaxSize;
        private Integer threadAliveSeconds;
    }

}
