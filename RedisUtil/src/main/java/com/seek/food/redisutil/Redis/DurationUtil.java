package com.seek.food.redisutil.Redis;

import java.time.Duration;

public class DurationUtil {
    public static Duration getMillisDuration(long duration) {
        return Duration.ofMillis(duration);
    }
}
