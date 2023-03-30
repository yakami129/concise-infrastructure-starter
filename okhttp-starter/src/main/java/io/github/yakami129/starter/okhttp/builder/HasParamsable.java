package io.github.yakami129.starter.okhttp.builder;

import java.util.Map;

/**
 * Created by alan on 16/3/1.
 */
public interface HasParamsable {

    OkHttpRequestBuilder params(Map<String, Object> params);

    OkHttpRequestBuilder addParams(String key, String val);
}
