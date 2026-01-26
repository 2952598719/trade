package com.orosirian.trade.coupon.db.dao;

import com.orosirian.trade.coupon.db.model.CouponBatch;

import java.util.List;

public interface CouponBatchDao {

    boolean insertCouponBatch(CouponBatch couponBatch);

    boolean deleteCouponBatchById(long id);

    CouponBatch queryCouponBatchById(long id);

    boolean updateCouponBatch(CouponBatch couponBatch);

    List<CouponBatch> queryCouponBatchList();

    boolean updateSendCouponBatchCount(Long id);

}
