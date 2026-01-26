package com.orosirian.trade.coupon.db.mappers;

import com.orosirian.trade.coupon.db.model.CouponBatch;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface CouponBatchMapper {

    int deleteByPrimaryKey(Long id);

    int insert(CouponBatch row);

    int insertSelective(CouponBatch row);

    CouponBatch selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(CouponBatch row);

    int updateByPrimaryKey(CouponBatch row);

    // 自定义语句
    List<CouponBatch> queryCouponBatchList();

    int updateSendCouponBatchCount(Long id);

}