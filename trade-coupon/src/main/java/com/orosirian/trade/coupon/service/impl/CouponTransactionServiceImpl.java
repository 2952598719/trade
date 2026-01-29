package com.orosirian.trade.coupon.service.impl;

import com.orosirian.trade.coupon.db.mappers.CouponMapper;
import com.orosirian.trade.coupon.db.model.Coupon;
import com.orosirian.trade.coupon.service.CouponTransactionService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Instant;

@Slf4j
@Service
public class CouponTransactionServiceImpl implements CouponTransactionService {

    @Autowired
    private CouponMapper couponMapper;

    @Override
    public boolean tryCoupon(long userId, long orderId, long couponId) {
        Coupon coupon = couponMapper.queryCouponById(couponId);

        if (coupon == null) {           // 检查优惠券是否存在
            log.error("coupon not exist: {}", couponId);
            return false;
        }
        if (coupon.getStatus() != 0) {  // 判断优惠券是否已经使用
            log.error("coupon already used: {}", couponId);
            return false;
        }

        coupon.setStatus(3);            // 状态改为冻结
        coupon.setOrderId(orderId);     // 记录对应的订单号
        boolean res = couponMapper.updateCoupon(coupon) > 0;
        if (res) {
            log.info("coupon lock success: {}", couponId);
            return true;
        }
        return false;
    }

    @Override
    public boolean commitCoupon(long userId, long orderId, long couponId) {
        log.info("commit coupon: {}", couponId);
        Coupon coupon = couponMapper.queryCouponById(couponId);
        if (coupon == null) {           // 再次检查优惠券是否存在
            log.error("coupon not exist: {}", couponId);
            return false;
        }
        coupon.setStatus(1);
        coupon.setUsedTime(Instant.now());
        couponMapper.updateCoupon(coupon);
        return true;
    }

    @Override
    public boolean cancelCoupon(long userId, long orderId, long couponId) {
        log.info("cancel coupon: {}", couponId);
        Coupon coupon = couponMapper.queryCouponById(couponId);
        coupon.setStatus(0);    // 订单状态恢复为未使用
        coupon.setOrderId(0L);
        couponMapper.updateCoupon(coupon);
        return true;
    }

}
