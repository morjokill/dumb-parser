package com.morj.dumb.parser.redis;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import redis.clients.jedis.JedisPooled;

@Component
@RequiredArgsConstructor
public class RedisClient {
    private final JedisPooled jedisPooled;

    public String get(String uri) {
        return jedisPooled.get(uri);
    }

    public void set(String uri, String value) {
        jedisPooled.set(uri, value);
    }
}
