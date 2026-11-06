package seek.util.ai.langchain4j.util.autoconfigure;

import dev.langchain4j.model.chat.ChatModel;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import seek.util.ai.langchain4j.util.config.PropertiesConfig;
import seek.util.ai.langchain4j.util.support.RedisChatMemoryStore;
import seek.util.ai.langchain4j.util.util.ChatUtil;
import seek.util.db.redis.util.RedisUtil;
import dev.langchain4j.memory.chat.ChatMemoryProvider;
import dev.langchain4j.memory.chat.MessageWindowChatMemory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableAutoConfiguration(exclude = PropertiesConfig.class)
public class AutoConfig {

    @Bean
    public ChatUtil chatUtil(ChatModel chatModel){
        return new ChatUtil(chatModel);
    }

    @Bean
    public ChatMemoryProvider chatMemoryProvider(RedisUtil redisUtil,PropertiesConfig propertiesConfig) {
        return memoryId -> MessageWindowChatMemory.builder()
                .id(memoryId)
                // 滑动窗口，保留最近一定数量条消息，防止上下文无限膨胀
                .maxMessages(propertiesConfig.getRedisSessionMemory().getStoreMax())
                // 指定持久化存储实现，所有会话读写走Redis
                .chatMemoryStore(new RedisChatMemoryStore(redisUtil,propertiesConfig))
                .build();
    }

}