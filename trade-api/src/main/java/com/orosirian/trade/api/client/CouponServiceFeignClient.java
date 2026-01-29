package com.orosirian.trade.api.client;

import com.orosirian.trade.api.db.model.CouponVO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@FeignClient("coupon")
public interface CouponServiceFeignClient {

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
    );

    @PostMapping("/send/sendSynWithLock")
    public String sendCouponSynWithLock(@RequestParam("batchId") long batchId, @RequestParam("userId") long userId);

    @GetMapping("/query/user")
    public List<CouponVO> queryUserCouponList(@RequestParam("status") int status, @RequestParam("userId") long userId, @RequestParam("lastCouponId") long lastCouponId, @RequestParam("pageSize") int pageSize);

    @PostMapping("/send/sendBatch")
    public String sendUserCouponBatch(@RequestParam("batchId") long batchId, @RequestParam("userIds") String userIds);

    @PostMapping("/code/createCouponCodeList")
    public String createCouponCodeList(@RequestParam("batchId") long batchId, @RequestParam("codeNum") int codeNum);

    @PostMapping("/send/exchangeCouponWithCode/{userId}")
    public String exchangeCouponWithCode(@PathVariable("userId") long userId, @RequestParam("couponCode") String couponCode);





}
