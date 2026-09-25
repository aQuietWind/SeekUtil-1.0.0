package com.seek.util.langchain4jutil.RedisMemory;

import com.seek.util.redisutil.RedisUtil;
import dev.langchain4j.memory.chat.ChatMemoryProvider;
import dev.langchain4j.memory.chat.MessageWindowChatMemory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;

@Configuration
public class AiMemoryConfig {
    @Bean
    @Lazy
    @ConditionalOnMissingBean
    public RedisChatMemoryStore redisChatMemoryStore(RedisUtil redisUtil) {
        return new RedisChatMemoryStore(redisUtil,null);
    }

    @Value("${seek.util.langchain4j.config.redis-memory.store-max}")
    private Integer storeMax;
    @Bean
    @Lazy
    public ChatMemoryProvider chatMemoryProvider(RedisChatMemoryStore redisChatMemoryStore) {
        return memoryId -> MessageWindowChatMemory.builder()
                .id(memoryId)
                // 滑动窗口，保留最近一定数量条消息，防止上下文无限膨胀
                .maxMessages(storeMax)
                // 指定持久化存储实现，所有会话读写走Redis
                .chatMemoryStore(redisChatMemoryStore)
                .build();
    }
}