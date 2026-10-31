package com.seek.util.springaiutil;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.MessageChatMemoryAdvisor;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.ai.chat.memory.InMemoryChatMemoryRepository;
import org.springframework.ai.chat.memory.MessageWindowChatMemory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ChatConfig {
    @Bean
    public ChatMemory chatMemory() {
        return MessageWindowChatMemory.builder()
                .chatMemoryRepository(new InMemoryChatMemoryRepository())
                .maxMessages(20).build();
    }

    @Bean
    public ChatClient chatClient(ChatClient.Builder builder, ChatMemory chatMemory) {
        return builder
                // 全局系统提示词
                .defaultSystem("")
                .defaultAdvisors(new AiLogAdvisor(), MessageChatMemoryAdvisor.builder(chatMemory).build())
                .build();
    }
    @Bean
    public ChatUtil chatUtil(ChatClient chatClient) {
        return new ChatUtil(chatClient);
    }
}
