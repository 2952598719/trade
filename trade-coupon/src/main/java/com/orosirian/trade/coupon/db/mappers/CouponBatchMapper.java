package com.orosirian.trade.coupon.db.mappers;

import com.orosirian.trade.coupon.db.model.CouponBatch;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface CouponBatchMapper {

    int deleteCouponBatchById(Long id);

    int insertCouponBatch(CouponBatch row);

    int insertSelective(CouponBatch row);

    CouponBatch queryCouponBatchById(Long id);

    int updateCouponBatchSelective(CouponBatch row);

    int updateCouponBatch(CouponBatch row);

    // 自定义语句
    List<CouponBatch> queryCouponBatchList();

    int updateSendCouponBatchCount(Long batchId);

    String selectCouponRule(Long batchId);

}