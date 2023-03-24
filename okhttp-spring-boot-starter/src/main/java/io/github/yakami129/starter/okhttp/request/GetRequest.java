package io.github.yakami129.starter.okhttp.request;

import okhttp3.Request;
import okhttp3.RequestBody;

import java.util.Map;

/**
 * Created by alan on 15/12/14.
 */
public class GetRequest extends OkHttpRequest {

    public GetRequest(String url,
                      Object tag,
                      Map<String, Object> params,
                      Map<String, String> headers,
                      int id) {
        super(url, tag, params, headers, id);
    }

    @Override
    protected RequestBody buildRequestBody() {
        return null;
    }

    @Override
    protected Request buildRequest(RequestBody requestBody) {
        return builder.get().build();
    }


}
