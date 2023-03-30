package io.github.yakami129.starter.chatgpt.interceptor;

import com.alibaba.fastjson2.JSON;
import io.github.yakami129.starter.chatgpt.entity.BaseResponse;
import io.github.yakami129.starter.chatgpt.exception.ChatException;
import lombok.extern.slf4j.Slf4j;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.Objects;

/**
 * Created by alan on 2023/3/23.
 */
@Slf4j
public class ChatErrorInterceptor implements Interceptor {

    @NotNull
    @Override
    public Response intercept(@NotNull Chain chain) throws IOException {
        Request original = chain.request();
        Response response = chain.proceed(original);
        if (!response.isSuccessful()) {
            String errorMsgObj = response.body().string();
            BaseResponse baseResponse = JSON.parseObject(errorMsgObj, BaseResponse.class);
            String errorMessage = "未知错误";
            if (Objects.nonNull(baseResponse.getError())) {
                errorMessage = baseResponse.getError().getMessage();
            }
            log.error("[ERROR] {}", errorMessage);
            throw new ChatException(errorMessage);
        }
        return response;
    }
}
