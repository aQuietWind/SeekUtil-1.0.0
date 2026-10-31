package com.seek.util.langchain4jutil;

import com.seek.util.configobject.UtilObject.Exception.BizException;
import com.seek.util.configobject.UtilObject.Exception.ErrorCodeEnum;
import dev.langchain4j.data.audio.Audio;
import dev.langchain4j.data.image.Image;
import dev.langchain4j.data.message.*;
import dev.langchain4j.model.chat.ChatModel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.util.Base64;
import java.util.List;

@Slf4j
@Component
public class ChatUtil {

    private final ChatModel chatModel;
    @Autowired
    public ChatUtil(ChatModel chatModel) {
        this.chatModel = chatModel;
    }

    public AiMessage chatWithContent(List<ChatMessage> messages, Content ... userContents) {
        UserMessage msg=new UserMessage();
        //添加消息
        for (Content content : userContents) {
            if (content != null) msg.contents().add(content);
        }
        if (messages != null) {
            messages.add(msg);
            return chatModel.chat(messages).aiMessage();
        }
        return chatModel.chat(msg).aiMessage();
    }

    //文件转换为图片上下文
    public ImageContent fileToImage(MultipartFile image) {
        if (image == null|| image.isEmpty()) throw new BizException(ErrorCodeEnum.FILE_IS_EMPTY);
        try {
            return ImageContent.from(Image.builder()
                    .base64Data(Base64.getEncoder().encodeToString(image.getBytes()))
                    .mimeType(image.getContentType())
                    .build());
        }catch (Exception e){
            log.error("序列化图片为上下文时出错:",e);
            throw new RuntimeException(e);
        }
    }

    //文件转换为音乐上下文
    public AudioContent fileToAudio(MultipartFile audio) {
        if (audio == null|| audio.isEmpty()) throw new BizException(ErrorCodeEnum.FILE_IS_EMPTY);
        try {
            return AudioContent.from(Audio.builder()
                    .base64Data(Base64.getEncoder().encodeToString(audio.getBytes()))
                    .mimeType(audio.getContentType())
                    .build());
        }catch (Exception e){
            log.error("序列化图片为上下文时出错:",e);
            throw new RuntimeException(e);
        }
    }

    //文本转换为文字上下文
    public TextContent stringToText(String text) {
        if(text!=null&&!text.isBlank())return TextContent.from(text);
        return null;
    }

    //聊天并且附带照片
    public AiMessage chatWithImage(String text,MultipartFile image ,List<ChatMessage> messages) {
        //添加消息
        return chatWithContent(messages,fileToImage(image),stringToText(text));
    }

    //聊天并且附带音频
    public AiMessage chatWithAudio(String text,MultipartFile audio ,List<ChatMessage> messages) {
        //添加消息
        return chatWithContent(messages,fileToAudio(audio),stringToText(text));
    }
}
