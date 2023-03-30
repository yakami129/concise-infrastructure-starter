package io.github.yakami129.starter.netty.test;

import io.github.yakami129.starter.netty.NettyAutoConfiguration;
import io.github.yakami129.starter.netty.base.BaseTest;
import io.github.yakami129.starter.netty.model.Session;
import io.github.yakami129.starter.netty.service.SessionService;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Created by alan on 2023/2/23.
 */
@Slf4j
@SpringBootTest(classes = {
        NettyAutoConfiguration.class,
})
@TestPropertySource("classpath:application.yml")
class DefaultSessionServiceImplTest extends BaseTest {

    @Autowired
    private SessionService sessionService;

    @Test
    void add() {

        final Session s1 = createSession();

        final Session session = sessionService.get(s1.getId());
        assertThat(session).isNotNull();
    }


    @Test
    void delete() {

        final Session s1 = createSession();

        sessionService.delete(s1.getId());

        final Session session = sessionService.get(s1.getId());
        assertThat(session).isNull();
    }

    @Test
    void deleteLocalhost() {

        final Session s1 = createSession();
        final Session s2 = createSession();
        final Session s3 = createSession();

        // 清空当前host所有的session
        sessionService.deleteLocalhost();

        final List<Session> serviceAll = sessionService.findAll();
        assertThat(serviceAll).isEmpty();
    }

    @Test
    void updateState() {

        final Session s1 = createSession();

        sessionService.updateState(s1.getId(), Session.STATE_APNS);

        final Session session = sessionService.get(s1.getId());
        assertThat(session.getState()).isEqualTo(Session.STATE_APNS);
    }

    @Test
    void findAll() {

        final Session s1 = createSession();
        final Session s2 = createSession();
        final Session s3 = createSession();

        final List<Session> serviceAll = sessionService.findAll();
        assertThat(serviceAll).isNotEmpty();
        assertThat(serviceAll).hasSize(3);
    }

    @Test
    void attribute() {
        final Session s1 = createSession();
        sessionService.addAttribute(s1.getId(),"k1","v1");

        final String k1 = sessionService.getAttribute(s1.getId(), "k1");
        assertThat(k1).isEqualTo("v1");
    }


}