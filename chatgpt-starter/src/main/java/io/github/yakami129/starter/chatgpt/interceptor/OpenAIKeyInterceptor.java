package io.github.yakami129.starter.chatgpt.interceptor;

import cn.hutool.core.util.RandomUtil;
import cn.hutool.http.ContentType;
import cn.hutool.http.Header;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.List;

/**
 * Created by alan on 2023/3/23.
 */
public class OpenAIKeyInterceptor implements Interceptor {

    private List<String> openAIKeys;

    public OpenAIKeyInterceptor(List<String> openAIKeys) {
        this.openAIKeys = openAIKeys;
    }

    @NotNull
    @Override
    public Response intercept(@NotNull Chain chain) throws IOException {
        Request original = chain.request();
        String key = RandomUtil.randomEle(openAIKeys);
        Request request = original.newBuilder()
                .header(Header.AUTHORIZATION.getValue(), "Bearer " + key)
                .header(Header.CONTENT_TYPE.getValue(), ContentType.JSON.getValue())
                .method(original.method(), original.body())
                .build();
        return chain.proceed(request);
    }

}
