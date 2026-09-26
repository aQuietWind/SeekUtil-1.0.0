package com.seek.util.springaiutil;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.beans.factory.annotation.Autowired;
import reactor.core.publisher.Flux;

public class ChatUtil {

    private final ChatClient chatClient;
    @Autowired
    public ChatUtil(ChatClient chatClient) {
        this.chatClient = chatClient;
    }

    public String chat(String msg) {
        return chatClient.prompt()
                .user(msg)
                .call()
                .content();
    }

    public Flux<String> streamChat(String msg) {
        return chatClient.prompt()
                .user(msg)
                .stream()
                .content();
    }
}