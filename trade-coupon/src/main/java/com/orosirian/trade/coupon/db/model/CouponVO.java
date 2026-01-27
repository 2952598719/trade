package com.orosirian.trade.coupon.db.model;

import lombok.Data;

@Data
public class CouponVO {     // 用于Coupon的信息显示，所以和Coupon本身的使用信息无关（orderId之类）

    private Long id;

    private Long userId;

    private Long batchId;

    private String couponName;

    private Integer status;

    private int couponType;     // 优惠券类型：1-满减，2-满折，3-直减

    private int grantType;      // 发放类型：1-用户领取，2-系统自动发放，3-兑换码兑换

    private String thresholdAmountStr;  // 优惠券使用的门槛金额文案

    private int discountAmount; // 优惠券使用的优惠金额

    private String startTimeToEndTime; // 有效日期

}
