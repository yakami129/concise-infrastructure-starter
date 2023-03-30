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
package io.github.yakami129.starter.netty.service;


import io.github.yakami129.starter.netty.model.Session;

import java.util.List;

/**
 * 存储连接信息，便于查看用户的链接信息
 */
public interface SessionService {

    Session get(long id);

    void add(Session session);

    void delete(long id);

    /**
     * 删除本机的连接记录
     */
    void deleteLocalhost();

    void updateState(long id, int state);

    void openApns(String uid, String deviceToken);

    void closeApns(String uid);

    List<Session> findAll();

    String getAttribute(long id, String key);

    void addAttribute(long id, String key, String value);
}
