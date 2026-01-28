package com.orosirian.trade.coupon.db.model;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class Task {

    private Long id;

    private Integer status;

    private Integer retryCount;

    private String bizType;

    private String bizId;

    private String bizParam;

    private LocalDateTime scheduleTime;

    private LocalDateTime modifiedTime;

    private LocalDateTime createTime;

}
