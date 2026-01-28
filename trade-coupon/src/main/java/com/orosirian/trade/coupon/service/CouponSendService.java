package com.orosirian.trade.coupon.service;

import java.util.Set;

public interface CouponSendService {

    boolean sendUserCouponSynWithLock(long batchId, long userId);

    boolean sendUserCouponSyn(long batchId, long userId);

    boolean sendUserCouponBatch(long batchId, Set<Long> userIdSet);

}
