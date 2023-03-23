package com.github.yakami.starter.okhttp.request;

import com.alibaba.fastjson2.JSON;
import lombok.extern.slf4j.Slf4j;
import okhttp3.Response;

import java.io.IOException;

/**
 * Created by alan on 2022/12/13.
 */
@Slf4j
public class HttpResponseBody {

    private Response response;

    public HttpResponseBody(Response response) {
        this.response = response;
    }

    public String body() {
        try {
            return response.body().string();
        } catch (IOException e) {
            log.error(e.getMessage(), e);
            throw new IllegalStateException("获取http请求结果失败");
        }
    }

    public <E> E body(Class<E> clazz) {
        return JSON.parseObject(body(), clazz);
    }

}
