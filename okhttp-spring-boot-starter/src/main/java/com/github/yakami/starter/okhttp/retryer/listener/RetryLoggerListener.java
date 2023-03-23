package com.github.yakami.starter.okhttp.retryer.listener;

import com.github.rholder.retry.Attempt;
import com.github.rholder.retry.RetryListener;
import lombok.extern.slf4j.Slf4j;

/**
 * Created by alan on 2022/9/20.
 * 重试日志监听器
 */
@Slf4j
public class RetryLoggerListener implements RetryListener {

    @Override
    public <V> void onRetry(Attempt<V> attempt) {

        if (log.isDebugEnabled()) {
            long attemptNumber = attempt.getAttemptNumber();
            long delaySinceFirstAttempt = attempt.getDelaySinceFirstAttempt();
            boolean hasException = attempt.hasException();
            log.debug("[Retry] 当前第{}次重试", attemptNumber);
            log.debug("[Retry] 距离第一次重试的延迟：{}", delaySinceFirstAttempt);

            if (hasException) {
                log.debug("[Retry] 异常的重试结果：{}", attempt.getExceptionCause().toString());
            } else {
                log.debug("[Retry] 正常的重试结果：{}", attempt.getResult());
            }
        }
    }

}
