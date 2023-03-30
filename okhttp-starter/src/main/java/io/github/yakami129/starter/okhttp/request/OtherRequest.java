package io.github.yakami129.starter.okhttp.request;

import io.github.yakami129.starter.okhttp.OkHttpUtils;
import okhttp3.MediaType;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.internal.http.HttpMethod;
import org.apache.commons.lang3.StringUtils;

import java.util.Map;

/**
 * Created by alan on 16/2/23.
 */
public class OtherRequest extends OkHttpRequest {
    private static MediaType MEDIA_TYPE_PLAIN = MediaType.parse("text/plain;charset=utf-8");

    private RequestBody requestBody;
    private String method;
    private String content;

    public OtherRequest(RequestBody requestBody,
                        String content,
                        String method,
                        String url,
                        Object tag,
                        Map<String, Object> params,
                        Map<String, String> headers,
                        int id) {
        super(url, tag, params, headers, id);
        this.requestBody = requestBody;
        this.method = method;
        this.content = content;

    }

    @Override
    protected RequestBody buildRequestBody() {
        if (requestBody == null && StringUtils.isEmpty(content) && HttpMethod.requiresRequestBody(method)) {
            throw new IllegalArgumentException("requestBody and content can not be null in method:" + method);
        }

        if (requestBody == null && StringUtils.isNotEmpty(content)) {
            requestBody = RequestBody.create(MEDIA_TYPE_PLAIN, content);
        }

        return requestBody;
    }

    @Override
    protected Request buildRequest(RequestBody requestBody) {
        if (method.equals(OkHttpUtils.METHOD.PUT)) {
            builder.put(requestBody);
        } else if (method.equals(OkHttpUtils.METHOD.DELETE)) {
            if (requestBody == null)
                builder.delete();
            else
                builder.delete(requestBody);
        } else if (method.equals(OkHttpUtils.METHOD.HEAD)) {
            builder.head();
        } else if (method.equals(OkHttpUtils.METHOD.PATCH)) {
            builder.patch(requestBody);
        }

        return builder.build();
    }

}
