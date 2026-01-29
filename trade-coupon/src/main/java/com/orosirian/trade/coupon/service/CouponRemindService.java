package com.orosirian.trade.coupon.service;

import com.orosirian.trade.coupon.db.model.CouponRule;

public interface CouponRemindService {

    boolean insertRemindTask(long userId, long couponId, CouponRule rule);

    void runCouponRemindTask();

}
