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

    @Override
    public List<Coupon> queryUserCouponList(long userId) {
        String cacheKey = Constants.LIST_KEY_PREFIX + userId;
        String cacheContent = stringRedisTemplate.opsForValue().get(cacheKey);
        if (cacheContent != null) {
            return JSON.parseObject(cacheContent, new TypeReference<>() {});
        } else {
            List<Coupon> list = couponMapper.queryUserCouponList(userId);
            stringRedisTemplate.opsForValue().set(cacheKey, JSON.toJSONString(list));
            return list;
        }
    }

    @Override
    public List<Coupon> queryUserCouponListWithoutCache(long userId) {
        return couponMapper.queryUserCouponList(userId);
    }

}
