package com.orosirian.trade.coupon.db.mappers;

import com.orosirian.trade.coupon.db.model.Coupon;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface CouponMapper {

    int deleteByPrimaryKey(Long id);

    int insertCoupon(Coupon row);

    int insertSelective(Coupon row);

    Coupon selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(Coupon row);

    int updateByPrimaryKey(Coupon row);

    List<Coupon> queryUserCouponList(long userId);


}