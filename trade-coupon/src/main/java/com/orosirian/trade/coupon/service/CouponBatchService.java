package com.orosirian.trade.coupon.service;

import com.orosirian.trade.coupon.db.model.CouponBatch;

import java.util.List;

public interface CouponBatchService {

    boolean insertCouponBatch(CouponBatch couponBatch);

    List<CouponBatch> queryCouponBatchList();

    String selectCouponRule(long batchId);

}
