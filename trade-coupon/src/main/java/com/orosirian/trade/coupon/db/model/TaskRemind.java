package com.orosirian.trade.coupon.db.model;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class TaskRemind {

    long userId;

    long couponId;

    LocalDateTime remindTime;

}
