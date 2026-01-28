package com.orosirian.trade.coupon.db.model;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class TaskIdempotent {

    private long id;

    private String bizType;

    private String bizId;

    private LocalDateTime createTime;

}
