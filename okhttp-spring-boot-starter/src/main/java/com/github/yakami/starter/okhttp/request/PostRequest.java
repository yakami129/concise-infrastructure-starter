package com.github.yakami.starter.okhttp.request;

import com.github.yakami.starter.okhttp.builder.PostBuilder;
import lombok.extern.slf4j.Slf4j;
import okhttp3.*;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;

import java.io.UnsupportedEncodingException;
import java.net.FileNameMap;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

/**
 * Created by alan on 2022/9/15.
 */
@Slf4j
public class PostRequest extends OkHttpRequest {

    private static MediaType MEDIA_APPLICATION_JSON = MediaType.parse("application/json;charset=UTF-8");

    private List<PostBuilder.FileInput> files;

    private String body;

    public PostRequest(String url, Object tag, Map<String, Object> params, Map<String, String> headers, int id, List<PostBuilder.FileInput> files, String body) {
        super(url, tag, params, headers, id);
        this.files = files;
        this.body = body;
    }

    @Override
    protected RequestBody buildRequestBody() {

        RequestBody requestBody;

        if (StringUtils.isNotBlank(body)) {
            requestBody = RequestBody.create(MEDIA_APPLICATION_JSON, body);
        } else {
            MultipartBody.Builder formBuilder = new MultipartBody.Builder()
                    .setType(MultipartBody.FORM);
            addParams(formBuilder);
            requestBody = formBuilder.build();
        }

        return requestBody;
    }

    @Override
    protected Request buildRequest(RequestBody requestBody) {
        return this.builder.post(requestBody).build();
    }


    private void addParams(MultipartBody.Builder builder) {

        // 填充参数
        if (Objects.nonNull(params) && !params.isEmpty()) {
            for (String key : params.keySet()) {

                String value = Optional.ofNullable(params.get(key))
                        .map(Object::toString)
                        .orElse(StringUtils.EMPTY);

                builder.addPart(Headers.of("Content-Disposition", "form-data; name=\"" + key + "\""), RequestBody.create(MultipartBody.FORM, value));
            }
        }

        // 填充文件参数
        if (CollectionUtils.isNotEmpty(files)) {
            for (int i = 0; i < files.size(); i++) {
                PostBuilder.FileInput fileInput = files.get(i);
                RequestBody fileBody = RequestBody.create(MediaType.parse(guessMimeType(fileInput.filename)), fileInput.file);
                builder.addFormDataPart(fileInput.paramName, fileInput.filename, fileBody);
            }
        }

    }

    private String guessMimeType(String path) {
        FileNameMap fileNameMap = URLConnection.getFileNameMap();
        String contentTypeFor = null;
        try {
            contentTypeFor = fileNameMap.getContentTypeFor(URLEncoder.encode(path, "UTF-8"));
        } catch (UnsupportedEncodingException e) {
            log.warn("[WARN] run getContentTypeFor error");
        }

        if (contentTypeFor == null) {
            contentTypeFor = "application/octet-stream";
        }
        return contentTypeFor;
    }
}
