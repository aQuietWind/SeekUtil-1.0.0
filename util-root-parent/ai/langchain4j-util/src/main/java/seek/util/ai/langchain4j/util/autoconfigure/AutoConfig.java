package seek.util.ai.langchain4j.util.autoconfigure;

import dev.langchain4j.model.chat.ChatModel;
import dev.langchain4j.store.memory.chat.ChatMemoryStore;
import seek.util.ai.langchain4j.util.config.PropertiesConfig;
import seek.util.ai.langchain4j.util.support.RedisChatMemoryStore;
import seek.util.ai.langchain4j.util.util.ChatUtil;
import seek.util.db.redis.util.util.Redis.RedisUtil;
import dev.langchain4j.memory.chat.ChatMemoryProvider;
import dev.langchain4j.memory.chat.MessageWindowChatMemory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AutoConfig {

    @Bean
    public ChatUtil chatUtil(ChatModel chatModel){
        return new ChatUtil(chatModel);
    }

    @Bean
    public PropertiesConfig propertiesConfig(){
        return new PropertiesConfig();
    }

    @Bean
    public ChatMemoryStore redisChatMemoryStore(RedisUtil redisUtil,PropertiesConfig propertiesConfig){
        return new RedisChatMemoryStore(redisUtil,propertiesConfig);
    }

    @Bean
    public ChatMemoryProvider chatMemoryProvider(ChatMemoryStore chatMemoryStore,PropertiesConfig propertiesConfig) {
        return memoryId -> MessageWindowChatMemory.builder()
                .id(memoryId)
                // 滑动窗口，保留最近一定数量条消息，防止上下文无限膨胀
                .maxMessages(propertiesConfig.getRedisSessionMemory().getStoreMax())
                // 指定持久化存储实现，所有会话读写走Redis
                .chatMemoryStore(chatMemoryStore)
                .build();
    }

}