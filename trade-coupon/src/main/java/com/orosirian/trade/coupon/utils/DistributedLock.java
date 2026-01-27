package com.orosirian.trade.coupon.utils;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.script.RedisScript;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.util.Collections;
import java.util.Random;

@Slf4j
@Component
public class DistributedLock {

    private static final Random RANDOM = new Random();

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    @SuppressWarnings("BusyWait")
    public boolean tryGetLock(String lockKey, String requestId, Duration expireTime) {
        int retryCount = 0;
        long waitTime = Constants.INIT_WAIT_TIME_MS;
        try {
            while(retryCount < Constants.MAX_RETRY_COUNT) {
                Boolean result = stringRedisTemplate.opsForValue().setIfAbsent(lockKey, requestId, expireTime);
                if (Boolean.TRUE.equals(result)) {
                    return true;
                }
                Thread.sleep(waitTime);  // BusyWait意思是线程一直占着。可以用虚拟线程来解决
                waitTime = Math.min(waitTime * 2, Constants.MAX_WAIT_TIME_MS) + RANDOM.nextInt(Constants.WAIT_TIME_DISTURB);     // 退避+抖动
                retryCount++;
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            log.error("tryLock error, lockKey: {}", lockKey, e);
        } catch (Exception e) {
            log.error("Try lock error, lockKey: {}", lockKey, e);
        }
        return false;
    }

    public boolean releaseLock(String lockKey, String requestId) {
        try {
            String script =
                "if redis.call('get', KEYS[1]) == ARGV[1] " +
                "then return redis.call('del', KEYS[1]) " +
                "else return 0 end";
            RedisScript<Long> redisScript = RedisScript.of(script, Long.class);
            Long result = stringRedisTemplate.execute(
                    redisScript,
                    Collections.singletonList(lockKey),
                    requestId
            );
            return result == 1L;
        } catch (Exception e) {
            log.error("Release lock error, lockKey: {}", lockKey, e);
            return false;
        }
    }

}
