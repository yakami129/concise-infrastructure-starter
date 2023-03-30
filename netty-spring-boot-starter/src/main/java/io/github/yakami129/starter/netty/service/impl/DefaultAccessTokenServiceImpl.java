package io.github.yakami129.starter.netty.service.impl;

import io.github.yakami129.starter.netty.component.redis.TokenRedisTemplate;
import io.github.yakami129.starter.netty.service.AccessTokenService;
import org.apache.commons.lang3.StringUtils;

import java.util.UUID;

/**
 * Created by alan on 2023/2/21.
 */
public class DefaultAccessTokenServiceImpl implements AccessTokenService {

    private TokenRedisTemplate tokenRedisTemplate;

    public DefaultAccessTokenServiceImpl(TokenRedisTemplate tokenRedisTemplate) {
        this.tokenRedisTemplate = tokenRedisTemplate;
    }

    @Override
    public String getUid(String token) {
        if (StringUtils.isBlank(token)) {
            return null;
        }
        return tokenRedisTemplate.get(token);
    }

    @Override
    public void delete(String token) {
        tokenRedisTemplate.delete(token);
    }

    /**
     * 方便调试，这里生成token为永不过期
     *
     * @param uid
     * @return
     */
    @Override
    public String generate(String uid) {
        String newToken = UUID.randomUUID().toString().replace("-", "");
        tokenRedisTemplate.save(newToken, uid);
        return newToken;
    }

}
