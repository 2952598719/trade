package com.orosirian.trade.coupon.service;

import com.orosirian.trade.coupon.db.model.CouponCode;

import java.util.List;

public interface CouponCodeService {

    boolean createCouponCodeList(long batchId, int codeNum);

    List<CouponCode> queryCouponCodeByBatch(long batchId);

    CouponCode getCouponCode(String couponCode);

    boolean updateCouponCode(CouponCode couponCode);

}
