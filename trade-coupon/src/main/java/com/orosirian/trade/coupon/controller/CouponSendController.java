package com.orosirian.trade.coupon.controller;

import com.orosirian.trade.coupon.service.CouponSendService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashSet;
import java.util.Set;

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
            return "优惠券发放失败, 原因:" + e.getMessage();
        }
    }

    @PostMapping("/send/sendBatch")
    @ResponseBody
    public String sendUserCouponBatch(@RequestParam("batchId") long batchId, @RequestParam("userIds") String userIds) {
        try {
            String[] userIdsSplit = userIds.split("\r\n");
            Set<Long> userIdSet = new HashSet<>();
            for (String userId : userIdsSplit) {
                if (userId != null && !userId.isEmpty()) {
                    userIdSet.add(Long.valueOf(userId));
                }
            }
            boolean _ = couponSendService.sendUserCouponBatch(batchId, userIdSet);
            return "优惠券批量发放成功";
        } catch (Exception e) {
            log.error("sendCouponBatch error, errorMessage:{}", e.getMessage());
            return "优惠券批量发放失败, 原因:" + e.getMessage();
        }
    }

}
