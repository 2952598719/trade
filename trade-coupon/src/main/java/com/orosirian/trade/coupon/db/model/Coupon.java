package com.orosirian.trade.coupon.db.model;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.Date;

@Data
public class Coupon {

    private Long id;

    private Long userId;

    private Long batchId;

    private String couponName;

    private Integer status;

    private Long orderId;

    private LocalDateTime receivedTime;

    private LocalDateTime validateTime;

    private LocalDateTime usedTime;

    private LocalDateTime createTime;

}