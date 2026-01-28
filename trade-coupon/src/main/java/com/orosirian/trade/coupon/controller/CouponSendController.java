package com.orosirian.trade.coupon.controller;

import com.orosirian.trade.coupon.db.model.CouponCode;
import com.orosirian.trade.coupon.service.CouponCodeService;
import com.orosirian.trade.coupon.service.CouponSendService;
import com.orosirian.trade.coupon.utils.BizException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;
import java.util.HashSet;
import java.util.Set;

@Slf4j
@RestController
public class CouponSendController {

    @Autowired
    private CouponSendService couponSendService;

    @Autowired
    private CouponCodeService couponCodeService;

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
            Set<Long> userIdSet = new HashSet<>();  // Set避免前端某个用户id重复导致多发
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

    @PostMapping("/send/exchangeCouponWithCode/{userId}")
    @ResponseBody
    public String exchangeCouponWithCode(@PathVariable("userId") long userId, @RequestParam("couponCode") String couponCode) {
        try {
            log.info("exchange code for coupon: Req userId: {}, couponCode: {}", userId, couponCode);
            CouponCode couponCodeInfo = couponCodeService.getCouponCode(couponCode);
            if (couponCodeInfo == null) {
                throw new BizException("兑换码无效");
            }

            boolean res = couponSendService.sendUserCouponSynWithLock(couponCodeInfo.getBatchId(), userId);
            if (!res) {
                throw new BizException("兑换失败");
            }

            couponCodeInfo.setUserId(userId);   // 在用户领取时才填写
            couponCodeInfo.setStatus(1);
            couponCodeInfo.setModifyTime(Instant.now());
            boolean _ = couponCodeService.updateCouponCode(couponCodeInfo);
            return "兑换成功";
        } catch (Exception e) {
            log.error("exchange code error ", e);
            return e.getMessage();
        }
    }


}
