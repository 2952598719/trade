package com.orosirian.trade.coupon.db.model;

import lombok.Data;

import java.time.Instant;

@Data
public class TaskIdempotent {

    private long id;

    private String bizType;

    private String bizId;

    private Instant createTime;

}
