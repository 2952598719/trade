package com.orosirian.trade.coupon.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.orosirian.trade.coupon.db.mappers.CouponMapper;
import com.orosirian.trade.coupon.db.model.Coupon;
import com.orosirian.trade.coupon.service.CouponQueryService;
import com.orosirian.trade.coupon.utils.Constants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CouponQueryServiceImpl implements CouponQueryService {

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    @Autowired
    private CouponMapper couponMapper;

    // 不返回所有的Coupon，而是返回从lastCouponId往后pageSize条，来适应前端滚动下拉
    @Override
    public List<Coupon> queryUserCouponList(int status, long userId, long lastCouponId, int pageSize) {
        String cacheKey;
        if (status == 0) {
            cacheKey = Constants.LIST_KEY_PREFIX + "unused:" + userId;
        } else if (status == 1) {
            cacheKey = Constants.LIST_KEY_PREFIX + "used:" + userId;
        } else {
            cacheKey = Constants.LIST_KEY_PREFIX + "expired:" + userId;
        }
        String cacheContent = stringRedisTemplate.opsForValue().get(cacheKey);
        if (cacheContent != null) {
            return JSON.parseObject(cacheContent, new TypeReference<>() {});
        } else {
            List<Coupon> list;
            if (status == 0) {
                list = couponMapper.queryUserCouponListUnused(userId, lastCouponId, pageSize);
                stringRedisTemplate.opsForValue().set(cacheKey, JSON.toJSONString(list));
            } else if (status == 1) {
                list = couponMapper.queryUserCouponListUsed(userId, lastCouponId, pageSize);
                stringRedisTemplate.opsForValue().set(cacheKey, JSON.toJSONString(list));
            } else {
                list = couponMapper.queryUserCouponListExpired(userId, lastCouponId, pageSize);
                stringRedisTemplate.opsForValue().set(cacheKey, JSON.toJSONString(list));
            }
            return list;
        }
    }

    @Override
    public List<Coupon> queryUserCouponListWithoutCache(long userId, long lastCouponId, int pageSize) {
        return couponMapper.queryUserCouponListUnused(userId, lastCouponId, pageSize);
    }

}
