package com.orosirian.trade.coupon.controller;

import com.orosirian.trade.coupon.service.CouponSendService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Slf4j
@Controller
public class CouponSendController {

    @Autowired
    private CouponSendService couponSendService;

    @RequestMapping("/coupon/sendSyn")
    @ResponseBody
    public String sendCouponSyn(long batchId, long userId) {
        try {
            log.info("batchId: {}, userId: {}", batchId, userId);
            couponSendService.sendUserCouponSyn(batchId, userId);
            return "优惠券发放成功";
        } catch (Exception e) {
            log.error("sendCouponSyn error, errorMessage:{}", e.getMessage());
            return "发放优惠券给用户失败,原因:" + e.getMessage();
        }
    }

    @RequestMapping("/coupon/sendSynWithLock")
    @ResponseBody
    public String sendCouponSynWithLock(long batchId, long userId) {
        try {
            log.info("batchId: {}, userId: {}", batchId, userId);
            couponSendService.sendUserCouponSynWithLock(batchId, userId);
            return "优惠券发放成功";
        } catch (Exception e) {
            log.error("sendCouponSyn error, errorMessage:{}", e.getMessage());
            return "发放优惠券给用户失败,原因:" + e.getMessage();
        }
    }

}
