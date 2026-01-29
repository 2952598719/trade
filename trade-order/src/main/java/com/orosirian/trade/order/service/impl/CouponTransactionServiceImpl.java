package com.orosirian.trade.order.service.impl;

import com.orosirian.trade.order.service.CouponTransactionService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class CouponTransactionServiceImpl implements CouponTransactionService {

    @Override
    public boolean tryCoupon(long userId, long couponId, long orderId) {
        return true;
    }

    @Override
    public boolean commitCoupon(long userId, long couponId, long orderId) {
        log.info("commit coupon: {}", couponId);
        return true;
    }

    @Override
    public boolean cancelCoupon(long userId, long couponId, long orderId) {
        log.info("cancel coupon: {}", couponId);
        return true;
    }

}
