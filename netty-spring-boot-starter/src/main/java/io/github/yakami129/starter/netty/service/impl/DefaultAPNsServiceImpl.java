package io.github.yakami129.starter.netty.service.impl;

import io.github.yakami129.starter.netty.model.Message;
import io.github.yakami129.starter.netty.service.APNsService;
import lombok.extern.slf4j.Slf4j;

/**
 * Created by alan on 2023/2/21.
 */
@Slf4j
public class DefaultAPNsServiceImpl implements APNsService {

    @Override
    public void push(Message message, String deviceToken) {
        log.warn("push unrealized");
    }
}
