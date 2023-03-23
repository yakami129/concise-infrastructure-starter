package com.github.yakami.starter.chatgpt;

import com.github.yakami.starter.chatgpt.constant.OpenAIConstants;
import com.github.yakami.starter.chatgpt.interceptor.ChatErrorInterceptor;
import com.github.yakami.starter.chatgpt.interceptor.OpenAIKeyInterceptor;
import com.github.yakami.starter.chatgpt.request.ChatRequest;
import com.google.common.collect.Lists;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import okhttp3.OkHttpClient;
import org.apache.commons.lang3.StringUtils;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.jackson.JacksonConverterFactory;

import java.net.Proxy;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

/**
 * Created by alan on 2023/3/23.
 */
@Slf4j
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ChatGPT {

    private OkHttpClient okHttpClient;
    private String openAIKey;


    @Builder.Default
    private String openaiHost = OpenAIConstants.OPENAI_HOST;
    @Builder.Default
    private Proxy proxy = Proxy.NO_PROXY;
    @Builder.Default
    private Long timeout = 30L;
    @Builder.Default
    private List<String> openAIKeys = Lists.newArrayList();


    private ChatRequest chatRequest;

    public ChatGPT init() {

        if (StringUtils.isNotBlank(openAIKey)) {
            this.openAIKeys.add(openAIKey);
        }

        OkHttpClient.Builder client = new OkHttpClient.Builder();
        client.addInterceptor(new OpenAIKeyInterceptor(openAIKeys));
        client.addInterceptor(new ChatErrorInterceptor());
        client.connectTimeout(timeout, TimeUnit.SECONDS);
        client.writeTimeout(timeout, TimeUnit.SECONDS);
        client.readTimeout(timeout, TimeUnit.SECONDS);
        if (Objects.nonNull(proxy)) {
            client.proxy(proxy);
        }
        OkHttpClient httpClient = client.build();

        this.okHttpClient = httpClient;
        final RxJava2CallAdapterFactory rxJava2CallAdapterFactory = RxJava2CallAdapterFactory.create();
        final JacksonConverterFactory jacksonConverterFactory = JacksonConverterFactory.create();

        chatRequest = new ChatRequest(openaiHost, okHttpClient, rxJava2CallAdapterFactory, jacksonConverterFactory);

        return this;
    }

    public ChatRequest chatRequest() {
        return chatRequest;
    }

}
