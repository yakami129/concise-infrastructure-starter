package io.github.yakami129.starter.chatgpt.client;

import io.github.yakami129.starter.chatgpt.entity.chat.ChatCompletion;
import io.github.yakami129.starter.chatgpt.entity.chat.ChatCompletionResponse;
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
