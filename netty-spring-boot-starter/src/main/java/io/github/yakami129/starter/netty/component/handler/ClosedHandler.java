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
import io.github.yakami129.starter.netty.constant.ChannelAttr;
import io.github.yakami129.starter.netty.constant.Constants;
import io.github.yakami129.starter.netty.model.Session;
import io.github.yakami129.starter.netty.handler.CIMRequestHandler;
import io.github.yakami129.starter.netty.model.SentBody;
import io.github.yakami129.starter.netty.service.SessionService;
import io.netty.channel.Channel;

import javax.annotation.Resource;
import java.util.Objects;

/**
 * 连接断开时，更新用户相关状态
 */
@CIMHandler(key = "client_closed")
public class ClosedHandler implements CIMRequestHandler {

	@Resource
	private SessionService sessionService;

	@Override
	public void process(Channel channel, SentBody message) {

		Long sessionId = channel.attr(Constants.SESSION_ID).get();

		if (sessionId == null){
			return;
		}

		/*
		 * ios开启了apns也需要显示在线，因此不删记录
		 */
		if (Objects.equals(channel.attr(ChannelAttr.CHANNEL).get(), Session.CHANNEL_IOS)){
			sessionService.updateState(sessionId, Session.STATE_INACTIVE);
			return;
		}

		sessionService.delete(sessionId);

	}

}
