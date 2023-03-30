package io.github.yakami129.starter.netty.base;

import cn.hutool.core.lang.UUID;
import com.alibaba.fastjson2.JSON;
import com.google.common.collect.Lists;
import io.github.yakami129.starter.netty.mock.SessionMock;
import io.github.yakami129.starter.netty.model.Session;
import io.github.yakami129.starter.netty.service.SessionService;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.AfterEach;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

/**
 * Created by alan on 2023/2/23.
 */
@Slf4j
public class BaseTest {

    @Autowired
    private SessionService sessionService;

    private final static List<Long> ids = Lists.newArrayList();

    @AfterEach
    public void afterEach() {
        ids.forEach(id -> {
            sessionService.delete(id);
        });
        sessionService.deleteLocalhost();
        log.info("clear size {}", ids.size());
    }

    public Session createSession() {
        final Session session = SessionMock.mockSession(UUID.randomUUID().toString());
        sessionService.add(session);
        ids.add(session.getId());
        log.info("session?{}", JSON.toJSONString(session));
        return session;
    }

}
