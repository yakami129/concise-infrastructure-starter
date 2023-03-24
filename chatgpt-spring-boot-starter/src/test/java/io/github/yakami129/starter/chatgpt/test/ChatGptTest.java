package io.github.yakami129.starter.chatgpt.test;

import io.github.yakami129.starter.chatgpt.ChatGPT;
import io.github.yakami129.starter.chatgpt.entity.chat.ChatCompletion;
import io.github.yakami129.starter.chatgpt.entity.chat.ChatCompletionResponse;
import io.github.yakami129.starter.chatgpt.entity.chat.Message;
import io.github.yakami129.starter.chatgpt.util.Proxys;
import org.junit.jupiter.api.Test;

import java.net.Proxy;
import java.util.Arrays;

/**
 * Created by alan on 2023/3/23.
 */
public class ChatGptTest {

    @Test
    public void test01() {

        //国内需要代理
        Proxy proxy = Proxys.http("127.0.0.1", 33210);

        ChatGPT chatGPT = ChatGPT.builder()
                .openAIKey("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")
                .proxy(proxy)
                .openaiHost("https://api.openai.com") //反向代理地址
                .build()
                .init();


        ChatCompletion chatCompletion = ChatCompletion
                .builder()
                .model(ChatCompletion.Model.GPT_3_5_TURBO.getName())
                .messages(Arrays.asList(Message.of("Redis如何做基准测试？")))
                .build();

        final ChatCompletionResponse chatCompletionResponse = chatGPT
                .chatRequest()
                .chatCompletion(chatCompletion);
        System.out.println(chatCompletionResponse.getChoices().get(0).getMessage().getContent());
    }

}
