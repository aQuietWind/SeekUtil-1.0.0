package seek.util.cloud.sentinel.util.config;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.boot.context.properties.ConfigurationProperties;
import seek.util.common.config.object.db.redis.RedisKeyData;


@ConfigurationProperties("seek.util.cloud.sentinel.config")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class PropertiesConfig {
    private String allServiceApiName="allServiceApi";
    private Authority authority;

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Authority{
        private String requestHeaderName="x-sentinel-authority";
    }


}
