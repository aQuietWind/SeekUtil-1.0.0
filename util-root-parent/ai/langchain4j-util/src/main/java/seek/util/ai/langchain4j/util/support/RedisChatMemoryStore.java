package seek.util.ai.langchain4j.util.support;

import org.springframework.beans.factory.annotation.Autowired;
import seek.util.ai.langchain4j.util.config.PropertiesConfig;
import seek.util.common.config.object.db.redis.RedisKeyData;
import seek.util.db.redis.util.util.Redis.RedisUtil;
import dev.langchain4j.data.message.ChatMessage;
import dev.langchain4j.data.message.ChatMessageDeserializer;
import dev.langchain4j.data.message.ChatMessageSerializer;
import dev.langchain4j.store.memory.chat.ChatMemoryStore;

import java.util.List;

//该存储方式不应该支持多模态,如图片音乐等等
//redis记忆存储会话
public class RedisChatMemoryStore implements ChatMemoryStore {

    private final RedisUtil redisUtil;
    private final RedisKeyData redisKey;

    @Autowired
    public RedisChatMemoryStore(RedisUtil redisUtil,PropertiesConfig propertiesConfig) {
        this.redisUtil = redisUtil;
        redisKey = propertiesConfig.getRedisSessionMemory().getRedisKey();
    }

    @Override
    public List<ChatMessage> getMessages(Object memoryId) {
        if(memoryId==null) return null;
        String json = redisUtil.getString(redisKey, memoryId);
        return ChatMessageDeserializer.messagesFromJson(json);
    }

    @Override
    public void updateMessages(Object memoryId, List<ChatMessage> messages) {
        if(memoryId==null) return;
        String json = ChatMessageSerializer.messagesToJson(messages);
        redisUtil.justSetStringWithExpire(redisKey,memoryId, json);
    }

    @Override
    public void deleteMessages(Object memoryId) {
        if(memoryId==null) return;
        redisUtil.delete(redisKey, memoryId);
    }
}