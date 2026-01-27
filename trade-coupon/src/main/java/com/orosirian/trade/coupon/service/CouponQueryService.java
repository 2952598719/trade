package com.orosirian.trade.coupon.service;

import com.orosirian.trade.coupon.db.model.Coupon;

import java.util.List;

public interface CouponQueryService {

    List<Coupon> queryUserCouponList(long userId);

    List<Coupon> queryUserCouponListWithoutCache(long userId);

}
