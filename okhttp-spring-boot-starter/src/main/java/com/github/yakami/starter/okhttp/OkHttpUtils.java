package com.github.yakami.starter.okhttp;

import com.github.yakami.starter.okhttp.Interceptor.LoggerInterceptor;
import com.github.yakami.starter.okhttp.builder.GetBuilder;
import com.github.yakami.starter.okhttp.builder.PostBuilder;
import com.github.yakami.starter.okhttp.command.DownloadFileCmd;
import okhttp3.OkHttpClient;

import java.io.File;
import java.util.concurrent.TimeUnit;

/**
 * Created by alan on 15/8/17.
 */
public class OkHttpUtils {

    public static final long DEFAULT_MILLISECONDS = 10000L;
    private volatile static OkHttpUtils mInstance;
    private OkHttpClient mOkHttpClient;

    // >>>>>>>>>>>> 通用的命令组件
    public DownloadFileCmd downloadFileCmd;

    public OkHttpUtils(OkHttpClient okHttpClient) {
        if (okHttpClient == null) {
            mOkHttpClient = new OkHttpClient()
                    .newBuilder()
                    .connectTimeout(DEFAULT_MILLISECONDS, TimeUnit.MILLISECONDS)
                    .readTimeout(DEFAULT_MILLISECONDS, TimeUnit.MILLISECONDS)
                    .writeTimeout(DEFAULT_MILLISECONDS, TimeUnit.MILLISECONDS)
                    .retryOnConnectionFailure(true)
                    .addInterceptor(new LoggerInterceptor())
                    .build();
        } else {
            mOkHttpClient = okHttpClient;
        }

        downloadFileCmd = new DownloadFileCmd(mOkHttpClient);
    }


    public static OkHttpUtils initClient(OkHttpClient okHttpClient) {
        if (mInstance == null) {
            synchronized (OkHttpUtils.class) {
                if (mInstance == null) {
                    mInstance = new OkHttpUtils(okHttpClient);
                }
            }
        }
        return mInstance;
    }

    public static OkHttpUtils getInstance() {
        return initClient(null);
    }

    public OkHttpClient getOkHttpClient() {
        return mOkHttpClient;
    }

    private DownloadFileCmd getDownloadFileCmd() {
        return downloadFileCmd;
    }

    // >>>>>>>>>>>> HTTP 基本的请求API

    public static GetBuilder get() {
        return new GetBuilder();
    }

    public static GetBuilder get(String url) {
        return new GetBuilder(url);
    }

    public static PostBuilder post() {
        return new PostBuilder();
    }

    public static PostBuilder post(String url) {
        return new PostBuilder(url);
    }

    // >>>>>>>>>>>> 下载文件相关API

    public static boolean downloadFile(String url, String directoryFilePath) {
        return getInstance().getDownloadFileCmd().downloadFile(url, directoryFilePath);
    }

    public static boolean downloadFile(String url, File directoryFile) {
        return getInstance().getDownloadFileCmd().downloadFile(url, directoryFile);
    }

    public static boolean downloadFile(String url, String fileAlias, File directoryFile) {
        return getInstance().getDownloadFileCmd().downloadFile(url, fileAlias, directoryFile);
    }

    public static class METHOD {
        public static final String GET = "GET";
        public static final String POST = "POST";
        public static final String HEAD = "HEAD";
        public static final String DELETE = "DELETE";
        public static final String PUT = "PUT";
        public static final String PATCH = "PATCH";
    }
}

