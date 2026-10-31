package seek.util.common.config.object.CaffeineData;

import lombok.Data;

@Data
public class CaffeineData {
    private int maxSize;
    private long expireSeconds;
}
