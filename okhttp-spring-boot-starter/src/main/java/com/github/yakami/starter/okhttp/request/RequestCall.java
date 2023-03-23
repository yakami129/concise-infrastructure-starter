package com.github.yakami.starter.okhttp.request;

import com.alibaba.fastjson2.JSONObject;
import com.github.yakami.starter.okhttp.Interceptor.LoggerInterceptor;
import com.github.yakami.starter.okhttp.OkHttpUtils;
import com.github.yakami.starter.okhttp.retryer.RetryerHandle;
import com.github.yakami.starter.okhttp.ssl.SslUtils;
import lombok.extern.slf4j.Slf4j;
import okhttp3.*;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

/**
 * Created by alan on 15/12/15.
 * 对OkHttpRequest的封装，对外提供更多的接口：cancel(),readTimeOut()...
 */
@Slf4j
public class RequestCall {

    private OkHttpRequest okHttpRequest;
    private Request request;
    private Call call;

    // 设置超时时间，默认10秒
    private long readTimeOut;
    private long writeTimeOut;
    private long connTimeOut;

    // 是否开启重试，默认开启
    private Boolean enableRetry = false;

    // 重试等待的时间间隔
    private long retrySleepTime = 200;

    private boolean ignoreSsl = false;

    // 重试次数
    private int retryAttemptNumber = 5;

    private OkHttpClient clone;

    public RequestCall(OkHttpRequest request) {
        this.okHttpRequest = request;
    }

    public RequestCall timeOut(long timeOut) {
        this.readTimeOut = timeOut;
        this.writeTimeOut = timeOut;
        this.connTimeOut = timeOut;
        return this;
    }

    public RequestCall readTimeOut(long readTimeOut) {
        this.readTimeOut = readTimeOut;
        return this;
    }

    public RequestCall writeTimeOut(long writeTimeOut) {
        this.writeTimeOut = writeTimeOut;
        return this;
    }

    public RequestCall connTimeOut(long connTimeOut) {
        this.connTimeOut = connTimeOut;
        return this;
    }

    public RequestCall enableRetry(Boolean enableRetry) {
        this.enableRetry = enableRetry;
        return this;
    }

    public RequestCall retrySleepTime(long retrySleepTime) {
        this.retrySleepTime = retrySleepTime;
        return this;
    }

    public RequestCall retryAttemptNumber(int retryAttemptNumber) {
        this.retryAttemptNumber = retryAttemptNumber;
        return this;
    }

    public RequestCall ignoreSsl(boolean ignoreSsl) {
        this.ignoreSsl = ignoreSsl;
        return this;
    }

    public Call buildCall(Callback callback) {

        request = generateRequest(callback);

        if (readTimeOut > 0 || writeTimeOut > 0 || connTimeOut > 0 || ignoreSsl) {

            readTimeOut = readTimeOut > 0 ? readTimeOut : OkHttpUtils.DEFAULT_MILLISECONDS;
            writeTimeOut = writeTimeOut > 0 ? writeTimeOut : OkHttpUtils.DEFAULT_MILLISECONDS;
            connTimeOut = connTimeOut > 0 ? connTimeOut : OkHttpUtils.DEFAULT_MILLISECONDS;

            OkHttpClient.Builder builder = OkHttpUtils.getInstance().getOkHttpClient().newBuilder()
                    .connectTimeout(connTimeOut, TimeUnit.MILLISECONDS)
                    .readTimeout(readTimeOut, TimeUnit.MILLISECONDS)
                    .writeTimeout(writeTimeOut, TimeUnit.MILLISECONDS)
                    .retryOnConnectionFailure(true)
                    .addInterceptor(new LoggerInterceptor());

            if (ignoreSsl) {
                try {
                    builder = builder
                            .sslSocketFactory(SslUtils.getIgnoreInitedSslContext().getSocketFactory(), SslUtils.IGNORE_SSL_TRUST_MANAGER_X509)
                            .hostnameVerifier(SslUtils.getIgnoreSslHostnameVerifier());
                } catch (Exception e) {
                    log.warn("[OKHTTP] ignoreSsl is error {}", e.getMessage());
                }
            }

            clone = builder.build();
            call = clone.newCall(request);
        } else {
            call = OkHttpUtils.getInstance().getOkHttpClient().newCall(request);
        }
        return call;
    }

    private Request generateRequest(Callback callback) {
        return okHttpRequest.generateRequest(callback);
    }

//    public void execute(Callback callback) {
//
//        buildCall(callback);
//
//        if (callback != null) {
//            callback.onBefore(request, getOkHttpRequest().getId());
//        }
//
//        OkHttpUtils.getInstance().execute(this, callback);
//    }

    public Call getCall() {
        return call;
    }

    public Request getRequest() {
        return request;
    }

    public OkHttpRequest getOkHttpRequest() {
        return okHttpRequest;
    }

    public Response execute() {
        if (enableRetry) {
            RetryerHandle retryerHandle = new RetryerHandle(retrySleepTime, retryAttemptNumber);
            return retryerHandle.retryer(() -> {
                return run();
            }, Response.class);
        } else {
            return run();
        }
    }

    public HttpResponseBody executeBody() {
        return new HttpResponseBody(execute());
    }

    private Response run() {
        buildCall(null);
        try {
            return call.execute();
        } catch (IOException e) {
            throw new IllegalStateException(e.getMessage());
        }
    }

    public <T> T execute(Class<T> clazz) {

        Response execute = execute();

        if (!execute.isSuccessful()) {
            throw new IllegalStateException("Failed to send the Http request");
        }

        final ResponseBody body = execute.body();
        try {
            return JSONObject.parseObject(body.string(), clazz);
        } catch (IOException e) {
            throw new IllegalStateException(e.getMessage());
        }
    }

    public void cancel() {
        if (call != null) {
            call.cancel();
        }
    }


}
