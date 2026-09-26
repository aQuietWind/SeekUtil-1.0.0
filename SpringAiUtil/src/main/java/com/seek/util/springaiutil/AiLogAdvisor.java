package com.seek.util.springaiutil;

import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.chat.client.ChatClientRequest;
import org.springframework.ai.chat.client.ChatClientResponse;
import org.springframework.ai.chat.client.advisor.api.CallAdvisor;
import org.springframework.ai.chat.client.advisor.api.CallAdvisorChain;
import org.springframework.ai.chat.messages.Message;

@Slf4j
public class AiLogAdvisor implements CallAdvisor {  
  
  
    @Override  
    public ChatClientResponse adviseCall( ChatClientRequest request, CallAdvisorChain chain) {
        StringBuilder sb = new StringBuilder();  
        for (Message msg : request.prompt().getInstructions()) {  
            sb.append("\n[").append(msg.getMessageType()).append("] ").append(msg.getText());  
        }  
        log.info("[AI请求]{}", sb);  
  
        // 执行调用链，调用大模型  
        ChatClientResponse response = chain.nextCall(request);  
  
        // 返回后置打印  
        if (response.chatResponse() != null) log.info("[AI响应]\n 响应文本:{} \n 耗费token:{}",  
                response.chatResponse().getResult().getOutput().getText(),response.chatResponse().getMetadata().getUsage());  
        return response;  
    }  

    @Override
    public int getOrder() {
        return 0;
    }

    @Override
    public String getName() {
        return this.getClass().getName();
    }
}