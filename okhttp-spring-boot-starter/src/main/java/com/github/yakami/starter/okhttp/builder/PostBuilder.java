package com.github.yakami.starter.okhttp.builder;

import cn.hutool.core.stream.StreamUtil;
import com.github.yakami.starter.okhttp.request.PostRequest;
import com.google.common.collect.Lists;
import com.github.yakami.starter.okhttp.request.RequestCall;
import lombok.Data;
import lombok.ToString;
import okhttp3.MediaType;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;

import java.io.File;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * Created by alan on 2022/9/15.
 */
public class PostBuilder extends OkHttpRequestBuilder<PostBuilder> implements HasParamsable {

    private static MediaType MEDIA_APPLICATION_JSON = MediaType.parse("application/json; charset=utf-8");

    private List<FileInput> files;

    private String body;

    public PostBuilder() {
    }

    public PostBuilder(String url) {
        this.url = url;
    }

    @Override
    public RequestCall build() {
        return new PostRequest(url, tag, params, headers, id, files, body).build();
    }

    public PostBuilder addParams(File... files) {

        if (ArrayUtils.isEmpty(files)) {
            throw new IllegalArgumentException("files is null");
        }

        initFiles();

        this.files.addAll(StreamUtil.of(files).map(FileInput::new).collect(Collectors.toList()));
        return this;
    }

    public PostBuilder addBody(String body) {

        if (StringUtils.isBlank(body)) {
            throw new IllegalArgumentException("body is null");
        }

        this.body = body;
        return this;
    }

    public PostBuilder addParams(String paramName, String fileName, File file) {

        if (StringUtils.isBlank(fileName) || Objects.isNull(file)) {
            throw new IllegalArgumentException("files is null");
        }

        initFiles();
        this.files.add(new FileInput(paramName, fileName, file));
        return this;
    }

    public PostBuilder addParams(String paramName, File file) {

        if (StringUtils.isBlank(paramName) || Objects.isNull(file)) {
            throw new IllegalArgumentException("files is null");
        }

        initFiles();
        this.files.add(new FileInput(paramName, file.getName(), file));
        return this;
    }

//    public OkHttpRequestBuilder addFile(String paramName, String fileName, byte[] fileBytes) {
//
//        if (StringUtils.isBlank(fileName) || ArrayUtils.isEmpty(fileBytes)) {
//            throw new IllegalArgumentException("files is null");
//        }
//
//        initFiles();
//        this.files.add(new FileInput(paramName, fileName, FileUtils.writeByteArrayToFile();));
//        return this;
//    }

    private void initFiles() {
        if (Objects.isNull(this.files)) {
            this.files = Lists.newArrayList();
        }
    }

    @Data
    @ToString
    public static class FileInput {

        public String paramName;
        public String filename;
        public File file;

        public FileInput(File file) {
            this.paramName = file.getName();
            this.filename = file.getName();
            this.file = file;
        }

        public FileInput(String paramName, String filename, File file) {
            this.paramName = paramName;
            this.filename = filename;
            this.file = file;
        }
    }

}
