/*
 * Copyright 2013-2022 Xia Jun(3979434@qq.com).
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 ***************************************************************************************
 *                                                                                     *
 *                        Website : http://www.farsunset.com                           *
 *                                                                                     *
 ***************************************************************************************
 */
package io.github.yakami129.starter.netty.component.redis;

import io.github.yakami129.starter.netty.component.event.MessageEvent;
import io.github.yakami129.starter.netty.component.event.SessionEvent;
import io.github.yakami129.starter.netty.constant.Constants;
import io.github.yakami129.starter.netty.model.Message;
import io.github.yakami129.starter.netty.model.Session;
import io.github.yakami129.starter.netty.utils.JSONUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.Objects;

@Component
public class SignalRedisTemplate extends StringRedisTemplate {

    @Value("${spring.profiles.active}")
    private String env;

    @Resource
    private ApplicationContext applicationContext;


    public SignalRedisTemplate(LettuceConnectionFactory connectionFactory) {
        super(connectionFactory);
        connectionFactory.setValidateConnection(true);
    }

    /**
     * 消息发送到 集群中的每个实例，获取对应长连接进行消息写入
     *
     * @param message
     */
    public void push(Message message) {
        if (isDev()) {
            applicationContext.publishEvent(new MessageEvent(message));
            return;
        }
        super.convertAndSend(Constants.PUSH_MESSAGE_INNER_QUEUE, JSONUtils.toJSONString(message));
    }

    /**
     * 消息发送到 集群中的每个实例，解决多终端在线冲突问题
     *
     * @param session
     */
    public void bind(Session session) {
        if (isDev()) {
            applicationContext.publishEvent(new SessionEvent(session));
            return;
        }
        super.convertAndSend(Constants.BIND_MESSAGE_INNER_QUEUE, JSONUtils.toJSONString(session));
    }

    /**
     * 本地调试环境下不走redis，避免lettuce 经常command timeout。
     *
     * @return
     */
    private boolean isDev() {
        return Objects.equals(env, "dev");
    }

}
