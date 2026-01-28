package com.orosirian.trade.coupon.db.mappers;

import com.orosirian.trade.coupon.db.model.CouponCode;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface CouponCodeMapper {

    int insertCouponCode(CouponCode couponCode);

    CouponCode queryCouponCodeByCode(String code);

    int updateCouponCode(CouponCode couponCode);

    List<CouponCode> queryCouponCodeByBatch(long batchId);

}
