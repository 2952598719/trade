package com.orosirian.trade.coupon.db.model;

import lombok.Data;

import java.time.Instant;

@Data
public class Coupon {

    private Long id;

    private Long userId;

    private Long batchId;

    private String couponName;

    private Integer status;

    private Long orderId;

    private Instant receivedTime;

    private Instant validateTime;

    private Instant usedTime;

    private Instant createTime;

}