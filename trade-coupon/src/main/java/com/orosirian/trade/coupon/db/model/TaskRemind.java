package com.orosirian.trade.coupon.db.model;

import lombok.Data;

import java.time.Instant;

@Data
public class TaskRemind {

    long userId;

    long couponId;

    Instant remindTime;

}
