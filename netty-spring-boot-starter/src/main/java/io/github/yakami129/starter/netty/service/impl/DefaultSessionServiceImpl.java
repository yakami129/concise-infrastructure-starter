package io.github.yakami129.starter.netty.service.impl;

import cn.hutool.core.lang.func.LambdaUtil;
import cn.hutool.core.util.IdUtil;
import com.google.common.collect.Lists;
import io.github.yakami129.starter.netty.component.redis.SignalRedisTemplate;
import io.github.yakami129.starter.netty.model.Session;
import io.github.yakami129.starter.netty.service.SessionService;
import io.github.yakami129.starter.netty.utils.JSONUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * Created by alan on 2023/2/21.
 */
@Slf4j
public class DefaultSessionServiceImpl implements SessionService {

    private SignalRedisTemplate signalRedisTemplate;

    private static final String SESSION_CACHE_KEY = "NETTY_SESSION_CACHE_KEY::";

    private static final String SESSION_HOST_KEY = "NETTY_SESSION_HOST_KEY::";

    // 时间单位为天
    private static final long SESSION_EXPIRE = 1;

    private final String host;

    public DefaultSessionServiceImpl(SignalRedisTemplate signalRedisTemplate) throws UnknownHostException {
        host = InetAddress.getLocalHost().getHostAddress();
        this.signalRedisTemplate = signalRedisTemplate;
    }

    @Override
    public Session get(long id) {
        final String cacheKey = getCacheKey(id);
        final Session session = getSession(cacheKey);
        return session;
    }

    @Override
    public void add(Session session) {
        long id = IdUtil.getSnowflakeNextId();
        session.setId(id);
        session.setHost(host);
        session.setAttribute(new LinkedHashMap<>());
        saveOrUpdate(session);
    }

    @Override
    public void delete(long id) {
        final String cacheKey = getCacheKey(id);
        signalRedisTemplate.delete(cacheKey);
        signalRedisTemplate.opsForHash().delete(getHostCacheKey(), cacheKey);
    }

    @Override
    public void deleteLocalhost() {

        // 清除当前host下所有的会话
        final Map<Object, Object> entries = signalRedisTemplate.opsForHash().entries(getHostCacheKey());
        final List<String> cacheKeys = entries.values().stream().map(a->a.toString()).collect(Collectors.toList());
        signalRedisTemplate.delete(cacheKeys);

        // 清除会话与host的映射关系
        signalRedisTemplate.delete(getHostCacheKey());
    }

    @Override
    public void updateState(long id, int state) {
        final String cacheKey = getCacheKey(id);
        final Session session = getSession(cacheKey);
        session.setState(state);
        saveOrUpdate(session);
    }

    private Session getSession(String cacheKey) {

        final String sessionJson = signalRedisTemplate.opsForValue().get(cacheKey);
        if (StringUtils.isBlank(sessionJson)) {
            return null;
        }

        final Session session = JSONUtils.fromJson(sessionJson, Session.class);
        return session;
    }

    @Override
    public void openApns(String uid, String deviceToken) {
        log.warn("openApns unrealized");
    }

    @Override
    public void closeApns(String uid) {
        log.warn("closeApns unrealized");
    }

    @Override
    public List<Session> findAll() {
        List<Session> sessions = Lists.newArrayList();
        final Set<String> keys = signalRedisTemplate.keys(SESSION_CACHE_KEY + "*");
        for (String key : keys) {
            sessions.add(getSession(key));
        }
        return sessions;
    }

    @Override
    public String getAttribute(long id, String key) {
        final String cacheKey = getCacheKey(id);
        final Session session = getSession(cacheKey);
        return session.getAttribute().get(key);
    }

    @Override
    public void addAttribute(long id, String key, String value) {
        final String cacheKey = getCacheKey(id);
        final Session session = getSession(cacheKey);
        session.getAttribute().put(key, value);
        saveOrUpdate(session);
    }

    public String getCacheKey(long id) {
        return StringUtils.join(SESSION_CACHE_KEY, id);
    }

    public String getHostCacheKey() {
        return StringUtils.join(SESSION_HOST_KEY, host);
    }

    private void saveOrUpdate(Session session) {



        // 保持当前会话信息
        final String cacheKey = getCacheKey(session.getId());
        signalRedisTemplate.opsForValue().set(cacheKey, JSONUtils.toJSONString(session), SESSION_EXPIRE, TimeUnit.DAYS);

        // 保持host与会话信息隐射
        final String hostCacheKey = getHostCacheKey();
        signalRedisTemplate.opsForHash().put(hostCacheKey, cacheKey, cacheKey);
        signalRedisTemplate.expire(hostCacheKey, SESSION_EXPIRE, TimeUnit.DAYS);

    }


}
