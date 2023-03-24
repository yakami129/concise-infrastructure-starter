package io.github.yakami129.starter.okhttp.Interceptor;

import com.alibaba.fastjson2.JSONObject;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import okhttp3.*;
import okio.Buffer;

import java.io.IOException;
import java.util.Objects;

/**
 * Created by alan on 2022/9/15.
 */
@Slf4j
public class LoggerInterceptor implements Interceptor {

    @Override
    public Response intercept(Chain chain) throws IOException {

        printRequestParam(chain.request());

        Response response = printResponse(chain.proceed(chain.request()));

        return response;
    }

    @SneakyThrows
    private Response printResponse(Response response) {

        String body = Objects.nonNull(response.body()) ? response.body().string() : "";

        if (log.isDebugEnabled()) {

            HttpUrl url = response.request().url();
            String method = response.request().method();
            boolean successful = response.isSuccessful();
            int code = response.code();
            String protocol = response.protocol().name();
            String message = response.message();

            log.debug("[OKHTTP] ============== Response log ==============");
            log.debug("[OKHTTP] {} {}", method, url);
            log.debug("[OKHTTP] successful : {}", successful);
            log.debug("[OKHTTP] code : {}", code);
            log.debug("[OKHTTP] protocol : {}", protocol);
            log.debug("[OKHTTP] message : {}", message);
            log.debug("[OKHTTP] body: {}", body);
        }

        Response clone = response.newBuilder().build();
        ResponseBody cloneBody = clone.body();
        ResponseBody responseBody = ResponseBody.create(cloneBody.contentType(), body);
        return response.newBuilder().body(responseBody).build();
    }

    private void printRequestParam(Request request) {

        if (log.isDebugEnabled()) {

            HttpUrl url = request.url();
            String method = request.method();
            Headers headers = request.headers();
            RequestBody body = request.body();
            Object tag = request.tag();

            log.debug("[OKHTTP] ============== Request log ==============");
            log.debug("[OKHTTP] {} {}", method, url);
            log.debug("[OKHTTP] headers : {}", JSONObject.toJSONString(headers.toMultimap()));
            log.debug("[OKHTTP] body : {}", Objects.nonNull(body) ? bodyToString(request) : "");
            log.debug("[OKHTTP] tag : {}", JSONObject.toJSONString(tag));

        }

    }

    private String bodyToString(final Request request) {
        try {
            final Request copy = request.newBuilder().build();
            final Buffer buffer = new Buffer();
            copy.body().writeTo(buffer);
            return buffer.readUtf8();
        } catch (IOException e) {
            return "something error when show requestBody.";
        }
    }

}
