package com.orosirian.trade.coupon.service.impl;

import com.alibaba.fastjson.JSON;
import com.orosirian.trade.coupon.db.mappers.CouponBatchMapper;
import com.orosirian.trade.coupon.db.model.CouponBatch;
import com.orosirian.trade.coupon.service.CouponBatchService;
import com.orosirian.trade.coupon.utils.Constants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CouponBatchServiceImpl implements CouponBatchService {

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    @Autowired
    private CouponBatchMapper couponBatchMapper;

    @Override
    public boolean insertCouponBatch(CouponBatch couponBatch) {
        boolean insertSuccess = couponBatchMapper.insertCouponBatch(couponBatch) > 0;
        stringRedisTemplate.opsForValue().set(Constants.RULE_KEY_PREFIX + couponBatch.getId(), couponBatch.getRule());
        return insertSuccess;
    }

    @Override
    public List<CouponBatch> queryCouponBatchList() {
        return couponBatchMapper.queryCouponBatchList();
    }

    @Override
    public String selectCouponRule(long batchId) {
        String rule = stringRedisTemplate.opsForValue().get(Constants.RULE_KEY_PREFIX + batchId);
        if (rule == null) {
            rule = couponBatchMapper.selectCouponRule(batchId);
        }
        return rule;
    }



}
