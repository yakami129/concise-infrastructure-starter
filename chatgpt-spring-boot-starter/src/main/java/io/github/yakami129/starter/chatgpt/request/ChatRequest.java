package io.github.yakami129.starter.chatgpt.request;

import io.github.yakami129.starter.chatgpt.client.ChatClient;
import io.github.yakami129.starter.chatgpt.entity.chat.ChatCompletion;
import io.github.yakami129.starter.chatgpt.entity.chat.ChatCompletionResponse;
import io.github.yakami129.starter.chatgpt.entity.chat.Message;
import io.reactivex.Single;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.jackson.JacksonConverterFactory;

import java.util.Arrays;
import java.util.List;

/**
 * Created by alan on 2023/3/23.
 */
public class ChatRequest {

    private ChatClient chatClient;
    private String openaiHost;
    private OkHttpClient okHttpClient;
    private RxJava2CallAdapterFactory rxJava2CallAdapterFactory;
    private JacksonConverterFactory jacksonConverterFactory;

    public ChatRequest(String openaiHost,
                       OkHttpClient okHttpClient,
                       RxJava2CallAdapterFactory rxJava2CallAdapterFactory,
                       JacksonConverterFactory jacksonConverterFactory) {

        this.openaiHost = openaiHost;
        this.okHttpClient = okHttpClient;
        this.rxJava2CallAdapterFactory = rxJava2CallAdapterFactory;
        this.jacksonConverterFactory = jacksonConverterFactory;
        this.chatClient = new Retrofit.Builder()
                .baseUrl(this.openaiHost)
                .client(okHttpClient)
                .addCallAdapterFactory(rxJava2CallAdapterFactory)
                .addConverterFactory(jacksonConverterFactory)
                .build()
                .create(ChatClient.class);
    }


    /**
     * 最新版的GPT-3.5 chat completion 更加贴近官方网站的问答模型
     *
     * @param chatCompletion 问答参数
     * @return 答案
     */
    public ChatCompletionResponse chatCompletion(ChatCompletion chatCompletion) {
        Single<ChatCompletionResponse> chatCompletionResponse = this.chatClient.chatCompletion(chatCompletion);
        return chatCompletionResponse.blockingGet();
    }

    /**
     * 简易版
     *
     * @param messages 问答参数
     */
    public ChatCompletionResponse chatCompletion(List<Message> messages) {
        ChatCompletion chatCompletion = ChatCompletion.builder().messages(messages).build();
        return this.chatCompletion(chatCompletion);
    }

    /**
     * 直接问
     */
    public String chat(String message) {
        ChatCompletion chatCompletion = ChatCompletion
                .builder()
                .messages(Arrays.asList(Message.of(message)))
                .build();
        ChatCompletionResponse response = this.chatCompletion(chatCompletion);
        return response.getChoices().get(0).getMessage().getContent();
    }

}
