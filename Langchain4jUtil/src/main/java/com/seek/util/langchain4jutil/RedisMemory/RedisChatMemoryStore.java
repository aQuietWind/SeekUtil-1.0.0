package com.seek.util.langchain4jutil.RedisMemory;

import com.seek.util.configobject.RedisData.RedisKeyData;
import com.seek.util.redisutil.RedisUtil;
import dev.langchain4j.data.message.ChatMessage;
import dev.langchain4j.data.message.ChatMessageDeserializer;
import dev.langchain4j.data.message.ChatMessageSerializer;
import dev.langchain4j.store.memory.chat.ChatMemoryStore;
import org.springframework.beans.factory.annotation.Value;

import java.util.List;

//该存储方式不应该支持多模态,如图片音乐等等
public class RedisChatMemoryStore implements ChatMemoryStore {

    private final RedisUtil redisUtil;
    @Value("${seek.util.langchain4j.config.redis-memory.redis-key}")
    private RedisKeyData redisKey;

    public RedisChatMemoryStore(RedisUtil redisUtil,RedisKeyData redisKey) {
        this.redisUtil = redisUtil;
        //提供第二种方案来传递key
        if (redisKey != null) this.redisKey = redisKey;
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
        redisUtil.trySetStringWithExpire(redisKey,memoryId, json);
    }

    @Override
    public void deleteMessages(Object memoryId) {
        if(memoryId==null) return;
        redisUtil.delete(redisKey, memoryId);
    }
}