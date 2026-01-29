package com.orosirian.trade.api.controller;

import com.alibaba.fastjson.JSON;
import com.orosirian.trade.api.client.CouponServiceFeignClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
public class ManagerController {

//    @Autowired
//    private CouponServiceClient couponServiceClient;

    @Autowired
    private CouponServiceFeignClient couponServiceFeignClient;

    @PostMapping("/batch/addCouponBatch")
    public String addCouponBatch(
            @RequestParam("batchName") String batchName,
            @RequestParam("couponName") String couponName,
            @RequestParam("couponType") int couponType,
            @RequestParam("grantType") int grantType,
            @RequestParam("totalCount") long totalCount,
            @RequestParam("startTime") String startTime,
            @RequestParam("endTime") String endTime,
            @RequestParam("thresholdAmount") int thresholdAmount,
            @RequestParam("discountAmount") int discountAmount
    ) {
        return couponServiceFeignClient.addCouponBatch(batchName, couponName, couponType,grantType, totalCount, startTime, endTime, thresholdAmount, discountAmount);
    }

    @PostMapping("/send/sendCouponSyn")
    public String sendCouponSyn(@RequestParam("batchId") long batchId, @RequestParam("userId") long userId) {
        return couponServiceFeignClient.sendCouponSynWithLock(batchId, userId);
    }

    @GetMapping("/query/user")
    public String queryUserCouponList(@RequestParam("status") int status, @RequestParam("userId") long userId, @RequestParam("lastCouponId") long lastCouponId, @RequestParam("pageSize") int pageSize) {
        // 1线程下的jmeter压测QPS（开启虚拟线程）：queryUserCouponsWithoutCache-96，queryUserCoupons-102
        return JSON.toJSONString(couponServiceFeignClient.queryUserCouponList(status, userId, lastCouponId, pageSize));
    }

    @PostMapping("/send/sendBatch")
    public String sendUserCouponBatch(@RequestParam("batchId") long batchId, @RequestParam("userIds") String userIds) {
        // \r\n用%0D%0A替代
        return couponServiceFeignClient.sendUserCouponBatch(batchId, userIds);
    }

    @PostMapping("/code/createCouponCodeList")
    public String createCouponCodeList(@RequestParam("batchId") long batchId, @RequestParam("codeNum") int codeNum) {
        return couponServiceFeignClient.createCouponCodeList(batchId, codeNum);
    }

    @PostMapping("/send/exchangeCouponWithCode/{userId}")
    public String exchangeCouponWithCode(@PathVariable("userId") long userId, @RequestParam("couponCode") String couponCode) {
        return couponServiceFeignClient.exchangeCouponWithCode(userId, couponCode);
    }

}
