package com.orosirian.trade.coupon.service;

import com.orosirian.trade.coupon.db.model.Coupon;

import java.util.List;

public interface CouponQueryService {

    List<Coupon> queryUserCouponList(int status, long userId, long lastCouponId, int pageSize);

    List<Coupon> queryUserCouponListWithoutCache(long userId, long lastCouponId, int pageSize);

}
