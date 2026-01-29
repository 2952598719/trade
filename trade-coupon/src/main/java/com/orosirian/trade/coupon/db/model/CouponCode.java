package com.orosirian.trade.coupon.db.model;

import lombok.Data;

import java.time.Instant;

@Data
public class CouponCode {

    private long id;

    private String code;

    private Long batchId;

    private Long userId;

    private Integer status;

    private Instant createTime;

    private Instant modifyTime;

}
