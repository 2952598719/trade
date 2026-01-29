package com.orosirian.trade.coupon.db.model;

import lombok.Data;

import java.time.Instant;

@Data
public class CouponBatch {

    private Long id;

    private String batchName;

    private String couponName;

    private Integer couponType;

    private Integer grantType;

    private Long totalCount;

    private Long availableCount;

    private Long assignCount;

    private Long usedCount;

    private String rule;

    private Integer status;

    private Instant createTime;

}