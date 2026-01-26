package com.orosirian.trade.coupon.service.impl;

import com.orosirian.trade.coupon.db.mappers.CouponBatchMapper;
import com.orosirian.trade.coupon.db.model.CouponBatch;
import com.orosirian.trade.coupon.service.CouponBatchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CouponBatchServiceImpl implements CouponBatchService {

    @Autowired
    private CouponBatchMapper couponBatchMapper;

    @Override
    public boolean insertCouponBatch(CouponBatch couponBatch) {
        return couponBatchMapper.insertCouponBatch(couponBatch) > 0;
    }

    @Override
    public List<CouponBatch> queryCouponBatchList() {
        return couponBatchMapper.queryCouponBatchList();
    }


}
