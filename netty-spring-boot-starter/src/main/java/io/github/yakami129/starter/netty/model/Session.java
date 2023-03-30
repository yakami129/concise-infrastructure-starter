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
package io.github.yakami129.starter.netty.model;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Session {

    public static final int STATE_ACTIVE = 0;
    public static final int STATE_APNS = 1;
    public static final int STATE_INACTIVE = 2;

    public static final String CHANNEL_IOS = "ios";
    public static final String CHANNEL_ANDROID = "android";
    public static final String CHANNEL_WINDOWS = "windows";
    public static final String CHANNEL_MAC = "mac";
    public static final String CHANNEL_WEB = "web";

    /**
     * 数据库主键ID
     */
    private Long id;

    /**
     * session绑定的用户账号
     */
    private String uid;

    /**
     * session在本台服务器上的ID
     */
    private String nid;

    /**
     * 客户端ID (设备号码+应用包名),ios为deviceToken
     */

    private String deviceId;

    /**
     * 终端设备型号
     */
    private String deviceName;

    /**
     * session绑定的服务器IP
     */
    private String host;

    /**
     * 终端设备类型
     */
    private String channel;

    /**
     * 终端应用版本
     */
    private String appVersion;

    /**
     * 终端系统版本
     */
    private String osVersion;

    /**
     * 终端语言
     */
    private String language;

    /**
     * 登录时间
     */
    private Long bindTime;

    /**
     * 经度
     */
    private Double longitude;

    /**
     * 维度
     */
    private Double latitude;

    /**
     * 位置
     */
    private String location;

    /**
     * 状态
     */
    private int state;

    /**
     * 会话属性
     */
    private Map<String, String> attribute;
}
