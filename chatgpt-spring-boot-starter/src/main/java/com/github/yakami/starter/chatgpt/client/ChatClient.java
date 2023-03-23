package com.github.yakami.starter.chatgpt.client;

import com.github.yakami.starter.chatgpt.entity.chat.ChatCompletion;
import com.github.yakami.starter.chatgpt.entity.chat.ChatCompletionResponse;
import io.reactivex.Single;
import retrofit2.http.Body;
import retrofit2.http.POST;

/**
 * Created by alan on 2023/3/23.
 */
public interface ChatClient {

    @POST("/v1/chat/completions")
    Single<ChatCompletionResponse> chatCompletion(@Body ChatCompletion chatCompletion);

}
