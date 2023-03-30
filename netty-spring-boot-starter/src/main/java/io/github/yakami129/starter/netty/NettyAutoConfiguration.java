package io.github.yakami129.starter.netty;

import io.github.yakami129.starter.netty.component.predicate.DefaultHandshakePredicate;
import io.github.yakami129.starter.netty.component.redis.SignalRedisTemplate;
import io.github.yakami129.starter.netty.component.redis.TokenRedisTemplate;
import io.github.yakami129.starter.netty.service.APNsService;
import io.github.yakami129.starter.netty.service.AccessTokenService;
import io.github.yakami129.starter.netty.service.SessionService;
import io.github.yakami129.starter.netty.service.impl.DefaultAPNsServiceImpl;
import io.github.yakami129.starter.netty.service.impl.DefaultAccessTokenServiceImpl;
import io.github.yakami129.starter.netty.service.impl.DefaultSessionServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PostConstruct;
import java.net.UnknownHostException;

@Configuration
@ComponentScan(basePackages = "io.github.yakami129.starter.netty")
@Slf4j
public class NettyAutoConfiguration {

    @PostConstruct
    public void bannerPrintf() {
        log.info("[CONFIG] loading netty-component successful");
    }

    @Bean
    @ConditionalOnMissingBean
    public AccessTokenService defaultAccessTokenService(TokenRedisTemplate tokenRedisTemplate) {
        log.info("[CONFIG] netty-component loading defaultAccessTokenService successful");
        return new DefaultAccessTokenServiceImpl(tokenRedisTemplate);
    }

    @Bean
    @ConditionalOnMissingBean
    public APNsService defaultAPNsService() {
        log.info("[CONFIG] netty-component loading defaultAPNsService successful");
        return new DefaultAPNsServiceImpl();
    }

    @Bean
    @ConditionalOnMissingBean
    public SessionService defaultSessionService(SignalRedisTemplate signalRedisTemplate) throws UnknownHostException {
        log.info("[CONFIG] netty-component loading defaultSessionService successful");
        return new DefaultSessionServiceImpl(signalRedisTemplate);
    }

    @Bean
    @ConditionalOnMissingBean
    public DefaultHandshakePredicate defaultHandshakePredicate() {
        log.info("[CONFIG] netty-component loading defaultHandshakePredicate successful");
        return new DefaultHandshakePredicate();
    }

}
