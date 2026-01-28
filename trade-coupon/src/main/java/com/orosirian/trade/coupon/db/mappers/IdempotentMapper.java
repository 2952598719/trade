package com.orosirian.trade.coupon.db.mappers;

import com.orosirian.trade.coupon.db.model.TaskIdempotent;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface IdempotentMapper {

    int insertIdempotent(TaskIdempotent idempotent);

    TaskIdempotent queryIdempotentByBiz(String bizType, String bizId);

}
