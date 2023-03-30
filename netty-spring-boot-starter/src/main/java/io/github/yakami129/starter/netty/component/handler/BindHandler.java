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
package io.github.yakami129.starter.netty.component.handler;

import io.github.yakami129.starter.netty.component.handler.annotation.CIMHandler;
import io.github.yakami129.starter.netty.component.redis.SignalRedisTemplate;
import io.github.yakami129.starter.netty.constant.ChannelAttr;
import io.github.yakami129.starter.netty.constant.Constants;
import io.github.yakami129.starter.netty.group.SessionGroup;
import io.github.yakami129.starter.netty.handler.CIMRequestHandler;
import io.github.yakami129.starter.netty.model.ReplyBody;
import io.github.yakami129.starter.netty.model.SentBody;
import io.github.yakami129.starter.netty.model.Session;
import io.github.yakami129.starter.netty.service.SessionService;
import io.netty.channel.Channel;
import org.springframework.http.HttpStatus;

import javax.annotation.Resource;

/**
 * 客户长连接 账户绑定实现
 */
@CIMHandler(key = "client_bind")
public class BindHandler implements CIMRequestHandler {

    @Resource
    private SessionService sessionService;

    @Resource
    private SessionGroup sessionGroup;

    @Resource
    private SignalRedisTemplate signalRedisTemplate;

    @Override
    public void process(Channel channel, SentBody body) {

        if (sessionGroup.isManaged(channel)) {
            return;
        }

        ReplyBody reply = new ReplyBody();
        reply.setKey(body.getKey());
        reply.setCode(HttpStatus.OK.value());
        reply.setTimestamp(System.currentTimeMillis());

        String uid = body.get("uid");
        Session session = new Session();
        session.setUid(uid);
        session.setNid(channel.attr(ChannelAttr.ID).get());
        session.setDeviceId(body.get("deviceId"));
        session.setChannel(body.get("channel"));
        session.setDeviceName(body.get("deviceName"));
        session.setAppVersion(body.get("appVersion"));
        session.setOsVersion(body.get("osVersion"));
        session.setLanguage(body.get("language"));

        channel.attr(ChannelAttr.UID).set(uid);
        channel.attr(ChannelAttr.CHANNEL).set(session.getChannel());
        channel.attr(ChannelAttr.DEVICE_ID).set(session.getDeviceId());
        channel.attr(ChannelAttr.LANGUAGE).set(session.getLanguage());

        /*
         *存储到数据库
         */
        sessionService.add(session);
        reply.put("sessionId", session.getId().toString());

        channel.attr(Constants.SESSION_ID).set(session.getId());

        /*
         * 添加到内存管理
         */
        sessionGroup.add(channel);

        /*
         *向客户端发送bind响应
         */
        channel.writeAndFlush(reply);

        /*
         * 发送上线事件到集群中的其他实例，控制其他设备下线
         */
        signalRedisTemplate.bind(session);
    }
}
