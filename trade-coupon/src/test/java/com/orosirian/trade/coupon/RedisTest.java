package com.orosirian.trade.coupon;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.StringRedisTemplate;

import static org.assertj.core.api.Assertions.*;

@SpringBootTest
public class RedisTest {

    @Autowired
    private StringRedisTemplate redisTemplate;

    @Test
    public void setValue() {
        redisTemplate.opsForValue().set("testName", "你好");
    }

    @Test
    void getValue() {
        assertThat(redisTemplate.opsForValue().get("testName")).isEqualTo("你好");
    }

}
