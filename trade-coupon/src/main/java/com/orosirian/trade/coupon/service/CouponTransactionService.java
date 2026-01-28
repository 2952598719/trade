package com.orosirian.trade.coupon.service;

public interface CouponTransactionService {

    boolean tryCoupon(long userId, long couponId, long orderId);

    boolean commitCoupon(long userId, long couponId, long orderId);

    boolean cancelCoupon(long userId, long couponId, long orderId);

}
