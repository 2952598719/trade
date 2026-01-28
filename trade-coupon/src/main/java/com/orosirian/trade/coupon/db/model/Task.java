package com.orosirian.trade.coupon.db.model;

import lombok.Data;

import java.time.Instant;

@Data
public class Task {

    private Long id;

    private Integer status;

    private Integer retryCount;

    private String bizType;

    private String bizId;

    private String bizParam;

    private Instant scheduleTime;

    private Instant modifiedTime;

    private Instant createTime;

}
