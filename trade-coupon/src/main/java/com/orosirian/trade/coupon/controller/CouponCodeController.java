package com.orosirian.trade.coupon.controller;

import com.orosirian.trade.coupon.service.CouponCodeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class CouponCodeController {

    @Autowired
    private CouponCodeService couponCodeService;

    @PostMapping("/code/createCouponCodeList")
    public String createCouponCodeList(long batchId, int codeNum) {
        if (couponCodeService.createCouponCodeList(batchId, codeNum)) {
            return "创建优惠券码成功";
        } else {
            return "创建优惠券码失败";
        }
    }

}
