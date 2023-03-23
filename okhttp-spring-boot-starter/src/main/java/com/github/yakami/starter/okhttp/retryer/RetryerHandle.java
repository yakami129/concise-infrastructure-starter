package com.github.yakami.starter.okhttp.retryer;

import com.github.rholder.retry.*;
import com.github.yakami.starter.okhttp.retryer.listener.RetryLoggerListener;
import com.google.common.base.Predicates;

import java.io.IOException;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.function.Supplier;

/**
 * Created by alan on 2022/9/23.
 */
public class RetryerHandle {

    // 重试等待的时间间隔
    private long retrySleepTime;

    // 重试次数
    private int retryAttemptNumber;

    public RetryerHandle(long retrySleepTime, int retryAttemptNumber) {
        this.retrySleepTime = retrySleepTime;
        this.retryAttemptNumber = retryAttemptNumber;
    }

    public <T> T retryer(Supplier<T> execute, Class<T> clazz) {

        Callable<T> callable = () -> {
            return execute.get(); // 业务逻辑
        };

        // 定义重试器
        Retryer<T> retryer = RetryerBuilder.<T>newBuilder()
                .retryIfResult(Predicates.<T>isNull()) // 如果结果为空则重试
                .retryIfExceptionOfType(IOException.class) // 发生IO异常则重试
                .retryIfRuntimeException() // 发生运行时异常则重试
                .withWaitStrategy(WaitStrategies.fixedWait(retrySleepTime, TimeUnit.MILLISECONDS))
                .withStopStrategy(StopStrategies.stopAfterAttempt(retryAttemptNumber))
                .withRetryListener(new RetryLoggerListener())
                .build();

        try {
            return retryer.call(callable); // 执行
        } catch (RetryException | ExecutionException e) { // 重试次数超过阈值或被强制中断
            throw new IllegalStateException("[ERROR] 重试次数超过阈值或被强制中断");
        }
    }

}
