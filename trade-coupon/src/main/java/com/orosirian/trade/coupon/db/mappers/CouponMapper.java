package com.orosirian.trade.coupon.db.mappers;

import com.orosirian.trade.coupon.db.model.Coupon;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface CouponMapper {

    int deleteByPrimaryKey(Long id);

    int insertCoupon(Coupon row);

    int insertSelective(Coupon row);

    Coupon queryCouponById(Long id);

    int updateByPrimaryKeySelective(Coupon row);

    int updateCoupon(Coupon coupon);

    List<Coupon> queryUserCouponListUnused(long userId, long lastCouponId, int pageSize);

    List<Coupon> queryUserCouponListUsed(long userId, long lastCouponId, int pageSize);

    List<Coupon> queryUserCouponListExpired(long userId, long lastCouponId, int pageSize);

    boolean isCouponInvalid(long couponId);

    int updateExpiredCoupon();

}