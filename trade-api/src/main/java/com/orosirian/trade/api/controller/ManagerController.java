package com.orosirian.trade.api.controller;

import com.orosirian.trade.api.client.CouponServiceClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
public class ManagerController {

    @Autowired
    private CouponServiceClient couponServiceClient;

    @PostMapping("/batch/addCouponBatchAction")
    public String addCouponBatchAction(
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
        return couponServiceClient.addCouponBatchAction(batchName, couponName, couponType,grantType, totalCount, startTime, endTime, thresholdAmount, discountAmount);
    }

    @PostMapping("/send/sendCouponSynAction")
    public String sendCouponSynAction(@RequestParam("batchId") long batchId, @RequestParam("userId") long userId) {
        return couponServiceClient.sendCouponSynWithLock(batchId, userId);
    }

    @GetMapping("/query/queryUserCoupons")
    public String queryUserCoupons(@RequestParam("userId") long userId) {
        // TODO 这里的逻辑应该是反序列化成List进行处理再序列化发给前端吗
        // 1线程下的jmeter压测QPS（开启虚拟线程）：queryUserCouponsWithoutCache-96，queryUserCoupons-102
        return couponServiceClient.queryUserCouponList(userId);
    }

    @PostMapping("/send/sendBatch")
    public String sendUserCouponBatch(@RequestParam("batchId") long batchId, @RequestParam("userIds") String userIds) {
        // \r\n用%0D%0A替代
        return couponServiceClient.sendUserCouponBatch(batchId, userIds);
    }

}
