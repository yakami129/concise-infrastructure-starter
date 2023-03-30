package io.github.yakami129.starter.netty.mock;

import io.github.yakami129.starter.netty.model.Session;

/**
 * Created by alan on 2023/2/23.
 */
public class SessionMock {

    public static Session mockSession(String uid) {
        Session session = new Session();
        session.setUid(uid);
        session.setNid("e4332179");
        session.setDeviceId("7e6ac325c84d4d9fa4ba743dd4fd0057");
        session.setDeviceName("Chrome");
        session.setChannel("web");
        session.setAppVersion("1.0.0");
        session.setOsVersion("110.0.0.0");
        session.setLanguage("zh-CN");
        session.setState(Session.STATE_ACTIVE);
        return session;
    }


}
