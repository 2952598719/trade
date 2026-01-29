package com.orosirian.trade.coupon.db.model;

import lombok.Data;

import java.time.Instant;

@Data
public class CouponRule {

    private int couponType;         // 优惠券类型：1-满减，2-满折，3-直减

    private int grantType;          // 发放类型：1-用户领取，2-系统自动发放，3-兑换码兑换

    private Instant startTime;         // 优惠券使用有效期开始时间

    private Instant endTime;           // 优惠券使用有效期结束时间

    private int thresholdAmount;    // 优惠券使用的门槛金额

    private int discountAmount;     // 优惠券使用的优惠金额


}
