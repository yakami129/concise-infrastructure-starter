package io.github.yakami129.starter.okhttp.builder;


import cn.hutool.core.net.url.UrlBuilder;
import io.github.yakami129.starter.okhttp.request.GetRequest;
import io.github.yakami129.starter.okhttp.request.RequestCall;

import java.util.Iterator;
import java.util.Map;
import java.util.Set;

/**
 * Created by alan on 15/12/14.
 */
public class GetBuilder extends OkHttpRequestBuilder<GetBuilder> implements HasParamsable {

    public GetBuilder() {
    }

    public GetBuilder(String url) {
        this.url = url;
    }

    @Override
    public RequestCall build() {
        if (params != null) {
            url = appendParams(url, params);
        }

        return new GetRequest(url, tag, params, headers, id).build();
    }

    protected String appendParams(String url, Map<String, Object> params) {
        if (url == null || params == null || params.isEmpty()) {
            return url;
        }

        UrlBuilder urlBuilder = UrlBuilder.of(url);
        Set<String> keys = params.keySet();
        Iterator<String> iterator = keys.iterator();
        while (iterator.hasNext()) {
            String key = iterator.next();
            urlBuilder.addQuery(key, params.get(key));
        }
        return urlBuilder.build();
    }

}
