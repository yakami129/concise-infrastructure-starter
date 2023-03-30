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
package io.github.yakami129.starter.netty.constant;

import io.netty.util.AttributeKey;

public interface ChannelAttr {
    AttributeKey<Integer> PING_COUNT = AttributeKey.valueOf("ping_count");
    AttributeKey<String> UID = AttributeKey.valueOf("uid");
    AttributeKey<String> CHANNEL = AttributeKey.valueOf("channel");
    AttributeKey<String> ID = AttributeKey.valueOf("id");
    AttributeKey<String> DEVICE_ID = AttributeKey.valueOf("device_id");
    AttributeKey<String> TAG = AttributeKey.valueOf("tag");
    AttributeKey<String> LANGUAGE = AttributeKey.valueOf("language");
}
