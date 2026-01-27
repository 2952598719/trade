package com.orosirian.trade.coupon.service;

public interface CouponSendService {

    boolean sendUserCouponSynWithLock(long batchId, long userId);

    boolean sendUserCouponSyn(long batchId, long userId);

}
