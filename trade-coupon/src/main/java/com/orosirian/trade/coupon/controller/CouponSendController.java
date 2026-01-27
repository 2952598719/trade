package com.orosirian.trade.coupon.controller;

import com.orosirian.trade.coupon.service.CouponSendService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
public class CouponSendController {

    @Autowired
    private CouponSendService couponSendService;

    @PostMapping("/send/sendSyn")
    @ResponseBody
    public String sendCouponSyn(@RequestParam("batchId") long batchId, @RequestParam("userId") long userId) {
        try {
            log.info("batchId: {}, userId: {}", batchId, userId);
            couponSendService.sendUserCouponSyn(batchId, userId);
            return "优惠券发放成功";
        } catch (Exception e) {
            log.error("sendCouponSyn error, errorMessage:{}", e.getMessage());
            return "发放优惠券给用户失败,原因:" + e.getMessage();
        }
    }

    @PostMapping("/send/sendSynWithLock")
    @ResponseBody
    public String sendCouponSynWithLock(@RequestParam("batchId") long batchId, @RequestParam("userId") long userId) {
        try {
            log.info("batchId: {}, userId: {}", batchId, userId);
            boolean _ = couponSendService.sendUserCouponSynWithLock(batchId, userId);
            return "优惠券发放成功";
        } catch (Exception e) {
            log.error("sendCouponSyn error, errorMessage:{}", e.getMessage());
            return "发放优惠券给用户失败,原因:" + e.getMessage();
        }
    }

}
