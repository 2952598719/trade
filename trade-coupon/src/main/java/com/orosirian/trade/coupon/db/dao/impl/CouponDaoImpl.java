package com.orosirian.trade.coupon.db.dao.impl;

import com.orosirian.trade.coupon.db.dao.CouponDao;
import com.orosirian.trade.coupon.db.mappers.CouponMapper;
import com.orosirian.trade.coupon.db.model.Coupon;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class CouponDaoImpl implements CouponDao {

    @Autowired
    private CouponMapper couponMapper;

    @Override
    public boolean insertCoupon(Coupon coupon) {
        // 对应于给用户发放优惠券
        int result = couponMapper.insert(coupon);
        return result > 0;
    }

}
